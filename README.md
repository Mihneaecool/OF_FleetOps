# OF_FleetOps
# Documentatie Proiect FleetOps

## Descriere Generala
Proiectul FleetOps reprezinta o platforma integrata destinata monitorizarii si gestionarii flotelor de transport. Sistemul este dezvoltat pe o arhitectura bazata pe microservicii, asigurand o separare clara a responsabilitatilor intre gestiunea datelor, logica de business si motorul de calcul matematic pentru rute.

## Arhitectura Tehnica
Sistemul este compus din trei piloni principali care interactioneaza intr-un mediu containerizat. Primul este backend-ul dezvoltat in Java 17 folosind framework-ul Spring Boot, responsabil pentru securitate, persistenta datelor si expunerea API-ului prin intermediul unui Gateway. Al doilea pilon este reprezentat de motorul de calcul dezvoltat in C++ 17, care utilizeaza framework-ul Crow pentru a oferi performanta ridicata in procesarea algoritmilor de distanta. Al treilea pilon este infrastructura de baze de date PostgreSQL 15, utilizata pentru stocarea informatiilor despre vehicule, utilizatori si comenzi.

## Integrare si Livrare Continua
Proiectul implementeaza un flux de tip CI/CD prin intermediul GitHub Actions. La fiecare actualizare a codului sursa, sistemul declanseaza automat un proces de validare care include compilarea artefactelor Java prin Maven, verificarea integritatii codului C++ folosind compilatorul g++ si instalarea dependentelor necesare precum asio si nlohmann-json. Pasul final al procesului de automatizare consta in verificarea configuratiei Docker Compose pentru a garanta ca toate serviciile pot fi orchestrate corect intr-un mediu de productie.



## Instructiuni de Utilizare
Pentru punerea in functiune a intregului ecosistem local, este necesara prezenta Docker Desktop. Procedura de pornire se realizeaza prin comanda docker compose up --build executata in directorul radacina. Odata ce serviciile sunt active, interfata de testare Swagger poate fi accesata la adresa locala corespunzatoare portului de Gateway, permitand vizualizarea metodelor HTTP disponibile.

## Exemplu de Functionare
Sistemul permite calcularea distantelor intre puncte geografice precise. Un test standard de validare implica transmiterea coordonatelor pentru ruta Viena (48.2082, 16.3738) catre Praga (50.0755, 14.4378). Motorul C++ preia aceste date, proceseaza geometria rutei si returneaza rezultatul catre backend-ul Java, care il pune la dispozitia utilizatorului final prin interfata web sau aplicatia de monitorizare.