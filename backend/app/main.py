from fastapi import FastAPI
from mangum import Mangum
from app.db.dynamodb import table
from app.routes.results import router as results_router
from app.routes.history import router as history_router
from app.routes.games import router as games_router
from app.routes.analytics import router as analytics_router
from app.routes.dashboard import router as dashboard_router
from app.routes.activity import router as activity_router
from app.routes.reminders import router as reminders_router
from app.routes.alerts import router as alerts_router
from app.routes.sync import router as sync_router
from app.routes.voice import router as voice_router
from app.routes.auth import router as auth_router
    
app = FastAPI(
    title="Cognitive Gaming & Memory Assistance API",
    version="1.0.0",
)


@app.get("/")
def root():
    return {
        "message": "Cognitive Gaming API is running",
        "status": "ok",
    }


@app.get("/health")
def health_check():
    return {
        "status": "healthy",
    }


app.include_router(results_router)
app.include_router(history_router)
app.include_router(games_router)
app.include_router(analytics_router)
app.include_router(dashboard_router)
app.include_router(activity_router)
app.include_router(reminders_router)
app.include_router(alerts_router)
app.include_router(sync_router)
app.include_router(voice_router)
app.include_router(auth_router)
handler = Mangum(app)