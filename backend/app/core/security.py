import json
from functools import lru_cache
from typing import Any

import httpx
from fastapi import HTTPException, Security, status
from fastapi.security import HTTPAuthorizationCredentials, HTTPBearer
from jose import jwt

from app.core.config import settings


bearer_scheme = HTTPBearer()


@lru_cache
def get_jwks() -> dict[str, Any]:
    response = httpx.get(
        f"{settings.cognito_issuer}/.well-known/jwks.json",
        timeout=10.0,
    )
    response.raise_for_status()
    return response.json()


def verify_cognito_token(
    credentials: HTTPAuthorizationCredentials = Security(bearer_scheme),
) -> dict[str, Any]:

    token = credentials.credentials

    try:
        header = jwt.get_unverified_header(token)
        kid = header.get("kid")

        if not kid:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid token header",
            )

        jwks = get_jwks()

        key = next(
            (
                key
                for key in jwks["keys"]
                if key["kid"] == kid
            ),
            None,
        )

        if key is None:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Token signing key not found",
            )

        payload = jwt.decode(
            token,
            key,
            algorithms=["RS256"],
            issuer=settings.cognito_issuer,
            options={
                "verify_aud": False,
            },
        )

        return payload

    except HTTPException:
        raise

    except Exception as exc:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail=f"Invalid or expired Cognito token: {exc}",
        )