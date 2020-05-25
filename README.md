# Aplikacja RCP

### Opis:
>Aplikacja RCP (rejestracji czasu pracy) – użytkownik dodaje/usuwa wpisy do bazy. Na ich podstawie obliczany jest czas pracy i powstają wykresy. Dane stanowią informacje dla przełożonych o postępach, jak również mogą stanowić „dziennik” dla pracownika (co zostało wykonane w pracy/czego brakuje).

### Funkcjonalności:
 - oparta na androidzie
 - podział PRZEŁOŻONY/PRACOWNIK
 - pracownik może brać udział w kilku projektach
 - wpisy w ramach projektu
 - wpis do bazy - rejestruje miejsce (dropdown list, pracodawca dodaje miejsca pracy, nazwa+współrzędne), godzinę, zdjęcie, opis, czas pracy, projekt)
 - raportuje do bazy danych
 - można dopisać miejsce działania - wybór z drop listy lista z bazy danych
 - wykresy (w liczbie 3), statystyki/podsumowania dla danego okresu czasu (dodatkowa funkcjonalność)

### Technologie:
 - Android
 - Kotlin, Kotlin Coroutines
 - Firebase
 - MVVM pattern
 - Android Jetpack
 - Room
 - Navigation
 - LiveData
