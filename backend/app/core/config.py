from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    aws_region: str = "ap-south-1"
    dynamodb_table_name: str = "phoenix-sih-data"


settings = Settings()