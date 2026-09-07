from fastapi import APIRouter
from decimal import Decimal

from app.db.dynamodb import table


router = APIRouter(
    prefix="/patients",
    tags=["Game History"],
)


# ============================================================
# GET GAME HISTORY
# ============================================================

@router.get("/{patient_id}/game-history")
def get_game_history(patient_id: str):

    response = table.query(
        KeyConditionExpression="PK = :pk AND begins_with(SK, :sk)",
        ExpressionAttributeValues={
            ":pk": f"PATIENT#{patient_id}",
            ":sk": "RESULT#",
        },
        ScanIndexForward=False,
    )

    items = response.get("Items", [])

    return {
        "patient_id": patient_id,
        "count": len(items),
        "results": items,
    }


# ============================================================
# GET ADAPTIVE DIFFICULTY FOR A SPECIFIC GAME
# ============================================================

@router.get("/{patient_id}/games/{game_id}/difficulty")
def get_adaptive_difficulty(patient_id: str, game_id: str):

    response = table.query(
        KeyConditionExpression="PK = :pk AND begins_with(SK, :sk)",
        FilterExpression="game_id = :game_id",
        ExpressionAttributeValues={
            ":pk": f"PATIENT#{patient_id}",
            ":sk": "RESULT#",
            ":game_id": game_id,
        },
        ScanIndexForward=True,
    )

    items = response.get("Items", [])

    # ========================================================
    # NO PREVIOUS RESULTS FOR THIS GAME
    # ========================================================

    if not items:
        return {
            "patient_id": patient_id,
            "game_id": game_id,
            "difficulty": 1,
            "reason": "No game history available for this game",
        }

    # ========================================================
    # CALCULATE AVERAGE ACCURACY
    # ========================================================

    accuracies = [
        Decimal(str(item.get("accuracy", 0)))
        for item in items
    ]

    average_accuracy = (
        sum(accuracies) / Decimal(len(accuracies))
    )

    # ========================================================
    # GET LATEST DIFFICULTY
    # ========================================================

    latest_difficulty = int(
        items[-1].get("difficulty", 1)
    )

    # ========================================================
    # ADAPTIVE DIFFICULTY RULES
    # ========================================================

    if average_accuracy >= Decimal("0.80"):

        new_difficulty = min(
            latest_difficulty + 1,
            5
        )

        reason = "High accuracy - increasing difficulty"

    elif average_accuracy <= Decimal("0.50"):

        new_difficulty = max(
            latest_difficulty - 1,
            1
        )

        reason = "Low accuracy - decreasing difficulty"

    else:

        new_difficulty = latest_difficulty

        reason = "Moderate accuracy - keeping difficulty"

    # ========================================================
    # RESPONSE
    # ========================================================

    return {
        "patient_id": patient_id,
        "game_id": game_id,
        "difficulty": new_difficulty,
        "average_accuracy": float(average_accuracy),
        "previous_difficulty": latest_difficulty,
        "reason": reason,
    }