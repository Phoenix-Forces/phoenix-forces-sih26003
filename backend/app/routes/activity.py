from fastapi import APIRouter

from app.db.dynamodb import table


router = APIRouter(
    prefix="/patients",
    tags=["Activity"],
)


@router.get("/{patient_id}/activity")
def get_patient_activity(patient_id: str):
    response = table.query(
        KeyConditionExpression="PK = :pk AND begins_with(SK, :sk)",
        ExpressionAttributeValues={
            ":pk": f"PATIENT#{patient_id}",
            ":sk": "RESULT#",
        },
        ScanIndexForward=False,
    )

    items = response.get("Items", [])

    activity = []

    for item in items:
        activity.append({
            "activity_type": "game",
            "patient_id": patient_id,
            "game_id": item.get("game_id"),
            "session_id": item.get("session_id"),
            "result_id": item.get("result_id"),
            "score": item.get("score"),
            "accuracy": item.get("accuracy"),
            "time_taken_seconds": item.get("time_taken_seconds"),
            "difficulty": item.get("difficulty"),
            "created_at": item.get("created_at"),
        })

    return {
        "patient_id": patient_id,
        "count": len(activity),
        "activities": activity,
    }