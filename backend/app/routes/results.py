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
    timestamp = datetime.now(timezone.utc).isoformat()

    item = {
        "PK": f"PATIENT#{result.patient_id}",
        "SK": f"RESULT#{timestamp}#{result_id}",

        "entity_type": "GAME_RESULT",
        "result_id": result_id,

        "patient_id": result.patient_id,
        "game_id": result.game_id,
        "session_id": result.session_id,

       "score": result.score,
"accuracy": Decimal(str(result.accuracy)),
"time_taken_seconds": Decimal(str(result.time_taken_seconds)),
"difficulty": result.difficulty,
        "created_at": timestamp,
    }

    table.put_item(Item=item)

    return {
        "status": "success",
        "message": "Game result saved",
        "result_id": result_id,
    }