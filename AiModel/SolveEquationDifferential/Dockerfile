FROM python:3

WORKDIR /app

COPY . /app

RUN python -m pip install --upgrade pip

RUN pip install Flask

RUN pip install torch

RUN pip install flask-cors

RUN pip install numpy

EXPOSE 5000

CMD ["python", "application.py"]
