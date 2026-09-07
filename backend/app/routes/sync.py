from datetime import datetime, timezone
from decimal import Decimal
from uuid import uuid4

from fastapi import APIRouter

from app.db.dynamodb import table
from app.schemas.sync import SyncRequest


router = APIRouter(
    prefix="",
    tags=["Offline Sync"],
)


def convert_floats_to_decimal(value):
    """
    Convert Python floats to Decimal recursively
    so DynamoDB can store nested data safely.
    """

    if isinstance(value, float):
        return Decimal(str(value))

    if isinstance(value, dict):
        return {
            key: convert_floats_to_decimal(item)
            for key, item in value.items()
        }

    if isinstance(value, list):
        return [
            convert_floats_to_decimal(item)
            for item in value
        ]

    return value


@router.post("/sync")
def sync_operations(sync_request: SyncRequest):

    synced_operations = []
    failed_operations = []

    for operation in sync_request.operations:

        try:
            synced_at = datetime.now(timezone.utc).isoformat()

            converted_data = convert_floats_to_decimal(
                operation.data
            )

            item = {
                "PK": f"PATIENT#{operation.patient_id}",
                "SK": f"SYNC#{operation.operation_id}",

                "entity_type": "SYNC_OPERATION",

                "operation_id": operation.operation_id,
                "patient_id": operation.patient_id,

                "operation_type": operation.operation_type,
                "synced_entity_type": operation.entity_type,

                "data": converted_data,

                "status": "synced",
                "synced_at": synced_at,
            }

            table.put_item(Item=item)

            synced_operations.append({
                "operation_id": operation.operation_id,
                "status": "synced",
            })

        except Exception as error:

            failed_operations.append({
                "operation_id": operation.operation_id,
                "status": "failed",
                "error": str(error),
            })

    return {
        "status": "success",
        "total_operations": len(sync_request.operations),
        "synced_count": len(synced_operations),
        "failed_count": len(failed_operations),
        "synced_operations": synced_operations,
        "failed_operations": failed_operations,
    }