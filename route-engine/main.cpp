#include "crow_all.h"
#include <vector>
#include <nlohmann/json.hpp>
#include <cmath>

using namespace std;
using json = nlohmann::json;

struct Node {
    double lat, lon;
};

int main() {
    crow::SimpleApp app;

    CROW_ROUTE(app, "/calculate-route").methods(crow::HTTPMethod::POST)
    ([](const crow::request& req) {
        // 1. Încărcăm JSON-ul primit de la Java
        auto x = crow::json::load(req.body);
        if (!x) return crow::response(400, "Invalid JSON");

        // 2. EXTRAGEM coordonatele reale trimise din Swagger
        // Java trimite "sLat", "sLon", "eLat", "eLon"
        double sLat = x["sLat"].d();
        double sLon = x["sLon"].d();
        double eLat = x["eLat"].d();
        double eLon = x["eLon"].d();

        // 3. GENERĂM rute între aceste puncte (Simulare drum real)
        // În loc de Dijkstra pe 3 puncte fixe, creăm o listă de puncte între Start și Finish
        json responseArray = json::array();

        int numSteps = 8; // Câte puncte să aibă drumul
        for (int i = 0; i <= numSteps; ++i) {
            double fraction = (double)i / numSteps;

            // Calculăm punctul intermediar (interpolare liniară)
            double currentLat = sLat + (eLat - sLat) * fraction;
            double currentLon = sLon + (eLon - sLon) * fraction;

            // Adăugăm un pic de "zgomot" ca să nu fie o linie perfect dreaptă (opțional)
            if (i > 0 && i < numSteps) {
                currentLat += (sin(i) * 0.005);
                currentLon += (cos(i) * 0.005);
            }

            responseArray.push_back({
                {"lat", currentLat},
                {"lon", currentLon}
            });
        }

        // 4. Trimitem înapoi datele dinamice
        crow::response res(responseArray.dump());
        res.add_header("Content-Type", "application/json");
        return res;
    });

    app.port(18080).multithreaded().run();
}