# Projektantrag: Seidlserver

Das Ziel dieses Projekts ist es via einem low-powered PC, in unserem Fall einem Raspberry Pi, einen Gameserver zu steuern, da falls dieser dauernd laufen sollte, große Stromkosten anfallen würden.



## Funktionalität

Dieses Projekt wird folgende Funktionalität haben:

- Starten des Gameservers via WoL (Wake on Lan)
- Stoppen und Neustarten des Gameservers via Shell Commands über die API
- Analytics zum Server anzeigen
- [LinuxGSM Gamerserver](https://linuxgsm.com/) steuern
  - Starten
  - Stoppen
  - Neustarten
  - Status anzeigen



## Aufbau

Dieses Projekt wird in drei Teile unterteilt sein:

- Frontend
- Backend
- API



### Frontend

Die Aufgabe des Frontends ist es, für gute User Erfahrung zu sorgen. Das Frontend wird am Raspberry Pi laufen und wird mit React geschrieben sein. Hier haben wir einige sehr frühe Mockups vorbereitet: 

![Landing_Page.png](https://trello-attachments.s3.amazonaws.com/60506fb547dbea47e0421de2/6050863af296345b255600ac/9aa62032d9d8db385af292fb5cfbd7db/Landing_Page.png)

![Overview.png](https://trello-attachments.s3.amazonaws.com/60506fb547dbea47e0421de2/6050863eb45a0f3e18dca430/026b06b47bfabd65c7220395e5f70d90/Overview.png)



### Backend

Das Backend wird mit Java Spring implementiert. Die Aufgabe des Backends wird sein, den Server via WoL zu starten, Befehle/Daten über die API zu bekommen/senden und für User Management zu sorgen. Das Backend soll auch auf dem Raspberry Pi laufen.



### API

Um uns Probleme mit Limitierungen von SSH über Programmierschnittstellen zu ersparen, richten wir eine API auf dem Gameserver selbst ein. Die Aufgabe der API wird sein, vorgefertigte Befehle auszuführen und Daten zurückzuliefern. Diese API wird auch mit Java Spring implementiert werden.



## Team

Das Team bestehend aus Jonas Seidl und Martin Trummer, wird sich in zwei Gebieten spezialisieren: Frontend und Backend.

| Teammitglied   | Aufgabe          |
| -------------- | ---------------- |
| Jonas Seidl    | Backend, DB      |
| Martin Trummer | Frontend, Design |



## User Stories

- As a user I want to see the state of the physical Server
- As a user I want to start, stop and restart the physical Server
- As a user I want to see the state of the individual Game Servers
- As a user I want to start, stop and restart the individual Game Servers
- As a user I want to see analytics about the physical server
- As a user I want to login in or request a registration



## Technische Schwerpunkte

- GUI
- Java Spring
- JWT
- Shell Commands mit Java
- Wake On Lan

