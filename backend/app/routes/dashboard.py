from fastapi import APIRouter
from decimal import Decimal

from app.db.dynamodb import table


router = APIRouter(
    prefix="/patients",
    tags=["Dashboard"],
)


@router.get("/{patient_id}/dashboard")
def get_patient_dashboard(patient_id: str):

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
            "summary": {
                "total_games": 0,
                "average_accuracy": 0.0,
                "average_score": 0.0,
                "average_time_seconds": 0.0,
                "current_difficulty": 1,
            },
            "recent_games": [],
        }

    # Calculate summary values
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

    current_difficulty = int(
        items[0].get("difficulty", 1)
    )

    # Keep the most recent 5 games for dashboard
    recent_games = items[:5]

    return {
        "patient_id": patient_id,
        "summary": {
            "total_games": len(items),
            "average_accuracy": float(average_accuracy),
            "average_score": float(average_score),
            "average_time_seconds": float(average_time),
            "current_difficulty": current_difficulty,
        },
        "recent_games": recent_games,
    }