FROM ubuntu:22.04

# Evităm întrebările de fus orar în timpul instalării
ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y \
    build-essential \
    libasio-dev \
    nlohmann-json3-dev \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiem tot (inclusiv main.cpp și crow_all.h)
COPY . .

# Compilăm. Folosim -I. ca să găsească headerele în folderul curent
RUN g++ main.cpp -o route-engine -lpthread

EXPOSE 18080

CMD ["./route-engine"]