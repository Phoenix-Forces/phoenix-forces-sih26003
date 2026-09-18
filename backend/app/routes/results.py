from datetime import datetime, timezone
from decimal import Decimal
from uuid import uuid4

from fastapi import APIRouter

from app.db.dynamodb import table
from app.schemas.common import GameResultCreate


router = APIRouter(
    prefix="/game-results",
    tags=["Game Results"],
)


@router.post("")
def create_game_result(result: GameResultCreate):
    result_id = str(uuid4())
    created_at = datetime.now(timezone.utc).isoformat()

    total_attempts = (
        result.correct_count
        + result.incorrect_count
    )

    accuracy = (
        result.correct_count / total_attempts
        if total_attempts > 0
        else 0.0
    )

    item = {
        "PK": f"PATIENT#{result.patient_id}",
        "SK": f"RESULT#{result.timestamp}#{result_id}",

        "entity_type": "GAME_RESULT",
        "result_id": result_id,

        "patient_id": result.patient_id,

        "game_type": result.game_type,
        "score": result.score,

        "correct_count": result.correct_count,
        "incorrect_count": result.incorrect_count,

        "time_taken_seconds": result.time_taken_seconds,

        "difficulty_level": result.difficulty_level,

        "timestamp": result.timestamp,
        "cognitive_domain": result.cognitive_domain,

        # Calculated by backend for analytics
        "accuracy": Decimal(str(round(accuracy, 4))),

        "created_at": created_at,
    }

    table.put_item(Item=item)

    return {
        "status": "success",
        "message": "Game result saved",
        "result_id": result_id,
        "accuracy": round(accuracy, 4),
    }