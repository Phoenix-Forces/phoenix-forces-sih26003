from fastapi import APIRouter, Depends

from app.core.security import verify_cognito_token


router = APIRouter(
    prefix="/auth",
    tags=["Authentication"],
)


@router.get("/me")
def get_current_user(
    payload: dict = Depends(verify_cognito_token),
):
    return {
        "status": "success",
        "user": {
            "user_id": payload.get("sub"),
            "email": payload.get("email"),
            "username": payload.get("username"),
            "groups": payload.get("cognito:groups", []),
        },
    }