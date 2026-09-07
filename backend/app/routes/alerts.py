from datetime import datetime, timezone
from uuid import uuid4

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel

from app.db.dynamodb import table


router = APIRouter(
    prefix="",
    tags=["Alerts"],
)


class AlertUpdate(BaseModel):
    status: str


# ---------------------------------------------------------
# GET ALERTS FOR A PATIENT
# ---------------------------------------------------------

@router.get("/patients/{patient_id}/alerts")
def get_patient_alerts(patient_id: str):

    response = table.query(
        KeyConditionExpression="PK = :pk AND begins_with(SK, :sk)",
        ExpressionAttributeValues={
            ":pk": f"PATIENT#{patient_id}",
            ":sk": "ALERT#",
        },
        ScanIndexForward=False,
    )

    items = response.get("Items", [])

    return {
        "patient_id": patient_id,
        "count": len(items),
        "alerts": items,
    }


# ---------------------------------------------------------
# CREATE ALERT
# ---------------------------------------------------------

@router.post("/patients/{patient_id}/alerts")
def create_alert(
    patient_id: str,
    alert_type: str,
    message: str,
    severity: str = "medium",
):

    alert_id = str(uuid4())
    created_at = datetime.now(timezone.utc).isoformat()

    item = {
        "PK": f"PATIENT#{patient_id}",
        "SK": f"ALERT#{alert_id}",

        "entity_type": "ALERT",

        "alert_id": alert_id,
        "patient_id": patient_id,

        "alert_type": alert_type,
        "message": message,
        "severity": severity,

        "status": "active",
        "created_at": created_at,
    }

    table.put_item(Item=item)

    return {
        "status": "success",
        "message": "Alert created",
        "alert_id": alert_id,
    }


# ---------------------------------------------------------
# UPDATE ALERT STATUS
# ---------------------------------------------------------

@router.patch("/alerts/{alert_id}")
def update_alert(
    alert_id: str,
    alert: AlertUpdate,
):

    response = table.scan(
        FilterExpression="alert_id = :alert_id",
        ExpressionAttributeValues={
            ":alert_id": alert_id,
        },
    )

    items = response.get("Items", [])

    if not items:
        raise HTTPException(
            status_code=404,
            detail="Alert not found",
        )

    item = items[0]

    updated_at = datetime.now(timezone.utc).isoformat()

    table.update_item(
        Key={
            "PK": item["PK"],
            "SK": item["SK"],
        },
        UpdateExpression="SET #status = :status, updated_at = :updated_at",
        ExpressionAttributeNames={
            "#status": "status",
        },
        ExpressionAttributeValues={
            ":status": alert.status,
            ":updated_at": updated_at,
        },
    )

    return {
        "status": "success",
        "message": "Alert updated",
        "alert_id": alert_id,
        "new_status": alert.status,
    }