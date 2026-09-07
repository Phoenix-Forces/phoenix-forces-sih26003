from datetime import datetime, timezone
from uuid import uuid4

from fastapi import APIRouter, HTTPException

from app.db.dynamodb import table
from app.schemas.reminders import ReminderCreate, ReminderUpdate


router = APIRouter(
    prefix="",
    tags=["Reminders"],
)


# ---------------------------------------------------------
# GET ALL REMINDERS FOR A PATIENT
# ---------------------------------------------------------

@router.get("/patients/{patient_id}/reminders")
def get_reminders(patient_id: str):

    response = table.query(
        KeyConditionExpression="PK = :pk AND begins_with(SK, :sk)",
        ExpressionAttributeValues={
            ":pk": f"PATIENT#{patient_id}",
            ":sk": "REMINDER#",
        },
        ScanIndexForward=True,
    )

    items = response.get("Items", [])

    return {
        "patient_id": patient_id,
        "count": len(items),
        "reminders": items,
    }


# ---------------------------------------------------------
# CREATE REMINDER
# ---------------------------------------------------------

@router.post("/reminders")
def create_reminder(reminder: ReminderCreate):

    reminder_id = str(uuid4())
    created_at = datetime.now(timezone.utc).isoformat()

    item = {
        "PK": f"PATIENT#{reminder.patient_id}",
        "SK": f"REMINDER#{reminder_id}",

        "entity_type": "REMINDER",

        "reminder_id": reminder_id,
        "patient_id": reminder.patient_id,

        "title": reminder.title,
        "reminder_type": reminder.reminder_type,
        "scheduled_time": reminder.scheduled_time,

        "recurrence": reminder.recurrence,
        "notes": reminder.notes,

        "status": "pending",
        "created_at": created_at,
    }

    table.put_item(Item=item)

    return {
        "status": "success",
        "message": "Reminder created",
        "reminder_id": reminder_id,
    }


# ---------------------------------------------------------
# UPDATE REMINDER
# ---------------------------------------------------------

@router.patch("/reminders/{reminder_id}")
def update_reminder(
    reminder_id: str,
    reminder: ReminderUpdate
):

    # Find reminder first
    response = table.scan(
        FilterExpression="reminder_id = :rid",
        ExpressionAttributeValues={
            ":rid": reminder_id,
        },
    )

    items = response.get("Items", [])

    if not items:
        raise HTTPException(
            status_code=404,
            detail="Reminder not found",
        )

    item = items[0]

    update_fields = reminder.model_dump(
        exclude_unset=True,
        exclude_none=True,
    )

    if not update_fields:
        raise HTTPException(
            status_code=400,
            detail="No fields provided for update",
        )

    update_expression_parts = []
    expression_attribute_names = {}
    expression_attribute_values = {}

    for index, (field, value) in enumerate(update_fields.items()):

        name_key = f"#field{index}"
        value_key = f":value{index}"

        update_expression_parts.append(
            f"{name_key} = {value_key}"
        )

        expression_attribute_names[name_key] = field
        expression_attribute_values[value_key] = value

    table.update_item(
        Key={
            "PK": item["PK"],
            "SK": item["SK"],
        },
        UpdateExpression="SET " + ", ".join(update_expression_parts),
        ExpressionAttributeNames=expression_attribute_names,
        ExpressionAttributeValues=expression_attribute_values,
    )

    return {
        "status": "success",
        "message": "Reminder updated",
        "reminder_id": reminder_id,
    }


# ---------------------------------------------------------
# DELETE REMINDER
# ---------------------------------------------------------

@router.delete("/reminders/{reminder_id}")
def delete_reminder(reminder_id: str):

    response = table.scan(
        FilterExpression="reminder_id = :rid",
        ExpressionAttributeValues={
            ":rid": reminder_id,
        },
    )

    items = response.get("Items", [])

    if not items:
        raise HTTPException(
            status_code=404,
            detail="Reminder not found",
        )

    item = items[0]

    table.delete_item(
        Key={
            "PK": item["PK"],
            "SK": item["SK"],
        }
    )

    return {
        "status": "success",
        "message": "Reminder deleted",
        "reminder_id": reminder_id,
    }


# ---------------------------------------------------------
# COMPLETE REMINDER
# ---------------------------------------------------------

@router.post("/reminders/{reminder_id}/complete")
def complete_reminder(reminder_id: str):

    response = table.scan(
        FilterExpression="reminder_id = :rid",
        ExpressionAttributeValues={
            ":rid": reminder_id,
        },
    )

    items = response.get("Items", [])

    if not items:
        raise HTTPException(
            status_code=404,
            detail="Reminder not found",
        )

    item = items[0]

    completed_at = datetime.now(timezone.utc).isoformat()

    table.update_item(
        Key={
            "PK": item["PK"],
            "SK": item["SK"],
        },
        UpdateExpression="SET #status = :status, completed_at = :completed_at",
        ExpressionAttributeNames={
            "#status": "status",
        },
        ExpressionAttributeValues={
            ":status": "completed",
            ":completed_at": completed_at,
        },
    )

    return {
        "status": "success",
        "message": "Reminder completed",
        "reminder_id": reminder_id,
        "completed_at": completed_at,
    }