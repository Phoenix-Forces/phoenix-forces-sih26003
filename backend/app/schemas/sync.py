from typing import Any, Dict

from pydantic import BaseModel, Field


class SyncOperation(BaseModel):
    operation_id: str = Field(..., min_length=1)
    patient_id: str = Field(..., min_length=1)

    operation_type: str = Field(..., min_length=1)
    entity_type: str = Field(..., min_length=1)

    data: Dict[str, Any] = Field(default_factory=dict)


class SyncRequest(BaseModel):
    operations: list[SyncOperation] = Field(default_factory=list)