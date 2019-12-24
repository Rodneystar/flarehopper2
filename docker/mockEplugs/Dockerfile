FROM python:3.5.9-stretch

WORKDIR /opt

RUN git clone --single-branch --branch mock_server https://github.com/Rodneystar/eplugs.git
RUN python3 -m pip install flask

CMD ["python3", "/opt/eplugs/eplugs.py"]