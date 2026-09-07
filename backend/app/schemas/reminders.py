from typing import Optional

from pydantic import BaseModel


class ReminderCreate(BaseModel):
    patient_id: str
    title: str
    reminder_type: str
    scheduled_time: str
    recurrence: Optional[str] = None
    notes: Optional[str] = None


class ReminderUpdate(BaseModel):
    title: Optional[str] = None
    reminder_type: Optional[str] = None
    scheduled_time: Optional[str] = None
    recurrence: Optional[str] = None
    notes: Optional[str] = None
    status: Optional[str] = None