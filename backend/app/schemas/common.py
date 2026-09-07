from pydantic import BaseModel, Field


class GameResultCreate(BaseModel):
    patient_id: str = Field(..., min_length=1)
    game_id: str = Field(..., min_length=1)
    session_id: str = Field(..., min_length=1)

    score: int = Field(..., ge=0)
    accuracy: float = Field(..., ge=0.0, le=1.0)
    time_taken_seconds: float = Field(..., gt=0)

    difficulty: int = Field(..., ge=1, le=5)