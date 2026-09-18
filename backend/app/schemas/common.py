from pydantic import BaseModel, Field


class GameResultCreate(BaseModel):
    patient_id: str = Field(..., min_length=1)

    game_type: str = Field(..., min_length=1)
    score: int = Field(..., ge=0, le=100)

    correct_count: int = Field(..., ge=0)
    incorrect_count: int = Field(..., ge=0)

    time_taken_seconds: int = Field(..., ge=0)

    difficulty_level: str = Field(..., min_length=1)

    # Android stores this as milliseconds since Unix epoch
    timestamp: int = Field(..., gt=0)

    cognitive_domain: str = Field(..., min_length=1)