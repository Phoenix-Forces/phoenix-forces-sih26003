from fastapi import APIRouter

from app.routes.history import get_adaptive_difficulty


router = APIRouter(
    prefix="/patients",
    tags=["Games"],
)


@router.get("/{patient_id}/games/{game_id}/config")
def get_game_config(patient_id: str, game_id: str):

    # Get the recommended difficulty for this patient and game
    difficulty_data = get_adaptive_difficulty(
        patient_id=patient_id,
        game_id=game_id,
    )

    difficulty = difficulty_data["difficulty"]

    return {
        "patient_id": patient_id,
        "game_id": game_id,
        "difficulty": difficulty,
        "config": {
            "difficulty": difficulty,
            "time_limit_seconds": 60,
            "max_attempts": 3,
        },
    }