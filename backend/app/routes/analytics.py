from fastapi import APIRouter
from decimal import Decimal

from app.db.dynamodb import table


router = APIRouter(
    prefix="/patients",
    tags=["Analytics"],
)


@router.get("/{patient_id}/analytics")
def get_patient_analytics(patient_id: str):

    # Get all game results for this patient
    response = table.query(
        KeyConditionExpression="PK = :pk AND begins_with(SK, :sk)",
        ExpressionAttributeValues={
            ":pk": f"PATIENT#{patient_id}",
            ":sk": "RESULT#",
        },
        ScanIndexForward=False,
    )

    items = response.get("Items", [])

    # No game history
    if not items:
        return {
            "patient_id": patient_id,
            "total_games": 0,
            "average_accuracy": 0.0,
            "average_score": 0.0,
            "average_time_seconds": 0.0,
            "current_difficulty": 1,
            "games": [],
        }

    # Calculate analytics
    accuracies = [
        Decimal(str(item.get("accuracy", 0)))
        for item in items
    ]

    scores = [
        Decimal(str(item.get("score", 0)))
        for item in items
    ]

    times = [
        Decimal(str(item.get("time_taken_seconds", 0)))
        for item in items
    ]

    average_accuracy = sum(accuracies) / Decimal(len(accuracies))
    average_score = sum(scores) / Decimal(len(scores))
    average_time = sum(times) / Decimal(len(times))

    # Most recent result
    latest = items[0]

    current_difficulty = int(
        latest.get("difficulty", 1)
    )

    return {
        "patient_id": patient_id,
        "total_games": len(items),
        "average_accuracy": float(average_accuracy),
        "average_score": float(average_score),
        "average_time_seconds": float(average_time),
        "current_difficulty": current_difficulty,
        "games": items,
    }