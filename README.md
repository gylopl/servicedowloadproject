﻿# servicedowloadproject

Aplikacja wykonana w celach edukacyjnych.
Zamierzeniem bylo zrozumienie działania serwisów i wysyłania informacji z serwisu do UI, adaptery i inne do poprawy

https://github.com/gylopl/servicedowloadproject/blob/master/app/src/main/java/makdroid/servicesproject/services/DownloadService.java
Pobieranie pliku za pomoca klasy Service\n
Aktualny progres wysyłany za pomocą broadcastlocalmenagera.\n
Możliwość przerwania pobierania pliku.\n
Gdy połączenie zostanie utracone pobierany plik zostanie usunięty.\n
Wspierane rozszerzenia "image/png", "image/jpeg", "audio/mpeg", "text/plain", "application/pdf".


https://github.com/gylopl/servicedowloadproject/blob/master/app/src/main/java/makdroid/servicesproject/services/intentService/DownloadIntentService.java
Pobieranie pliku za pomoca klasy IntentService\n
Aktualny progres wysyłany za pomocą receivera.\n
Brak możliwosci przerwania pobierania

