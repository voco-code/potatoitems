# Ультралегендарное Снаряжение — Fabric 1.21.11

## Сборка мода (один раз установи Java 21 — Temurin с adoptium.net)
1. Распакуй архив, например в C:\ultra-mod
2. В папке проекта открой cmd (в проводнике в адресной строке напиши cmd и Enter)
3. Windows:  gradlew.bat build        Mac/Linux:  ./gradlew build
   (первый раз качает всё нужное, 5-10 минут, нужен интернет)
4. Готовый файл: build/libs/ultra-legendary-3.0.0.jar  (НЕ -sources)
5. Кинь его в папку mods вместе с Fabric API (0.141.6+1.21.11, modrinth.com)

## Важно
- Удали из мира/игры старые Ultra_DataPack_v3 и Ultra_ResourcePack_v3 — мод уже содержит всё то же.
- Руда появляется только в НОВЫХ чанках. Для теста: /give @s ultra:rainbow_ore
- Для режима приключений: /function ultra:adventure/kit
- Меч: /give @s ultra:ultra_sword  (или рецепт: 4 ультра-алмаза по углам + незеритовая звезда в центре)
