from fastapi import APIRouter
from pydantic import BaseModel


router = APIRouter(
    prefix="/voice",
    tags=["Voice"],
)


# Supported languages for the SIH MVP
SUPPORTED_LANGUAGES = [
    {
        "code": "en",
        "name": "English",
    },
    {
        "code": "hi",
        "name": "Hindi",
    },
    {
        "code": "as",
        "name": "Assamese",
    },
]


class STTRequest(BaseModel):
    language: str
    audio_base64: str


class TTSRequest(BaseModel):
    language: str
    text: str


@router.get("/languages")
def get_voice_languages():

    return {
        "languages": SUPPORTED_LANGUAGES
    }


@router.post("/stt")
def speech_to_text(request: STTRequest):

    # Temporary SIH MVP implementation.
    # Actual speech recognition service will be connected later.

    return {
        "status": "success",
        "language": request.language,
        "text": "",
        "message": "STT service placeholder. Speech recognition service will be connected during integration.",
    }


@router.post("/tts")
def text_to_speech(request: TTSRequest):

    # Temporary SIH MVP implementation.
    # Actual TTS service will be connected later.

    return {
        "status": "success",
        "language": request.language,
        "text": request.text,
        "audio_base64": "",
        "message": "TTS service placeholder. Speech synthesis service will be connected during integration.",
    }