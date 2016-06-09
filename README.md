# servicedowloadproject

Aplikacja wykonana w celach edukacyjnych.
Zamierzeniem bylo zrozumienie działania serwisów i wysyłania informacji z serwisu do UI, adaptery i inne do poprawy


[DownloadService](../master/app/src/main/java/makdroid/servicesproject/services/DownloadService.java)
Pobieranie pliku za pomoca klasy Service\n
Aktualny progres wysyłany za pomocą broadcastlocalmenagera.\n
Możliwość przerwania pobierania pliku.\n
Gdy połączenie zostanie utracone pobierany plik zostanie usunięty.\n
Wspierane rozszerzenia "image/png", "image/jpeg", "audio/mpeg", "text/plain", "application/pdf".


[DownloadIntentService](../master/app/src/main/java/makdroid/servicesproject/services/intentService/DownloadIntentService.java)
Pobieranie pliku za pomoca klasy IntentService\n
Aktualny progres wysyłany za pomocą receivera.\n
Brak możliwosci przerwania pobierania

Filmik youtube z aplikacją 
[![VIDEOHERE](https://img.youtube.com/vi/TqcKyR9u640/0.jpg)](https://www.youtube.com/watch?v=TqcKyR9u640)
