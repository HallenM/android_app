# android_app

## Общая информация
Примечание: Данное мобильное приложение было создано в рамках диссертационной работы на соискание степени магистра в НГТУ.

  Приложение является мобильным клиентом, предоставляющим доступ к сервису HPC Community Cloud (HPC2C), разработанном в институте вычислительной математики и математической геофизики Сибирского отделения Российской академии наук (ИВМиМГ СО РАН). Данный сервис это программный инструментарий, объединяющий ресурсы различных высокопроизводительных вычислительных систем (ВВС) в единый сервис и предоставляющий сторонним программным системам и пользователям единую точку доступа для работы с этим сервисом. 

  Для удобства отслеживания выполнения поставленных задач, а также большей мобильности доступа к HPC2C был спроектирован мобильный интерфейс системы, предоставляющий пользователю приложения доступ к HPC2C и его основным функциям, необходимым пользователю для работы с сервисом.

  Для начала работы необходимо авторизироваться в системе (рисунок 1), после чего пользователь переходит в меню приложения (рисунок 2), где ему предоставляется возможность просмотра интересующих его подпунктов меню (рисунок 2, 4), а также просмотра файловой системы, хранящей файлы, доступные авторизованному пользователю (рисунок 3), например файлы созданных приложений, результаты выполненных задач, необходимые документы и так далее.

| ![Screenshot_2019-12-30-04-17-46-762_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94595006-682bc580-02b4-11eb-803c-1911d6f06af4.png)  | ![Screenshot_2019-12-30-04-23-16-623_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94595024-6feb6a00-02b4-11eb-81b0-d6449ba0033d.png) | ![Screenshot_2019-12-30-04-23-23-546_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94595134-990bfa80-02b4-11eb-8cc5-8fd86a3d880d.png) |

Рисунок 1 – Форма входа в систему

| ![Screenshot_2019-12-30-04-23-23-546_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94595134-990bfa80-02b4-11eb-8cc5-8fd86a3d880d.png)  | ![Screenshot_2019-12-30-04-23-31-114_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94595503-1f284100-02b5-11eb-9752-2e0e70763f24.png) |

Рисунок 2 – Меню приложения 

| ![Screenshot_2019-12-30-04-50-31-728_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94595845-8b0aa980-02b5-11eb-9e12-29386da1239c.png)  | ![Screenshot_2019-12-30-04-50-47-172_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94595977-b4c3d080-02b5-11eb-99cd-98c4eb3f157b.png) |

Рисунок 3 – Файловая система пользователя

| ![Screenshot_2019-12-30-04-23-37-032_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94596438-50edd780-02b6-11eb-824b-5638d1b35efa.png)  | ![Screenshot_2019-12-30-04-48-22-237_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94596463-59461280-02b6-11eb-9ae1-f29686d11b84.png) | ![Screenshot_2019-12-30-04-50-23-740_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94596536-767ae100-02b6-11eb-8dc4-66d4e042b658.png) |

Рисунок 4 – Списки приложений, моделей и кластеров

  Помимо просмотра имеющихся у пользователя данных, интерфейс предоставляет пользователю возможность постановки новой задачи на выполнение, отслеживание статуса её выполнения и просмотр результатов вычисления (рисунок 5).
  
| ![Screenshot_2019-12-30-04-49-07-602_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94596964-218b9a80-02b7-11eb-9fa0-ed5fe735d482.png)  | ![Screenshot_2019-12-30-04-49-23-821_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94597024-39fbb500-02b7-11eb-97e3-8181a34e0522.png) | ![Screenshot_2019-12-30-04-50-01-933_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94597042-408a2c80-02b7-11eb-93bc-50e2df54a533.png) |
  
Рисунок 5 – Постановка новой задачи на выполнение, её отображение в общем списке задач и просмотр результатов вычисления

## Профилирование

  Кроме того, в мобильный интерфейс доступа к функциональности HPC2C был встроен дополнительный модуль профилирования задач. В рамках работы под оценкой производительности понимается постановка на выполнение определённого приложения с изменяющимся количеством ядер для запуска, последующим сбором результатов с этих запусков и выводом собранных данных в удобном для запрашивающего виде.
 
 Для того чтобы воспользоваться данным модулем, необходимо в меню приложения выбрать из списка (рисунок 2) позицию просмотра доступных пользовательских приложений, после чего пользователь имеет возможность оценить производительность своего приложения, нажав на кнопку Measure application performance (рисунок 4), расположенную под списком приложений. После нажатия на кнопку пользователь попадает в окно профилировщика, где необходимо ввести данные, такие как общее имя для последующих запусков, минимальное и максимальное количество ядер для запуска, а также выбрать приложение для оценки и нажать кнопку Start. После начала работы профилировщика необходимо ожидать пока он не закончит выполнение и после всплывающей подсказки нажать на кнопку See result, которая открывает окно, где будут представлены результаты оценки производительности приложения в виде таблицы, содержащей в себе информацию о количестве ядер для каждого запуска вычислительного приложения, времени вычисления задач, эффективности и ускорению приложения относительно первого запуска (рисунок 6).

| ![Screenshot_2019-12-30-04-26-11-979_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94597943-724fc300-02b8-11eb-8e3e-842e2538a33e.png)  | ![Screenshot_2019-12-30-04-48-38-239_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94598136-b6db5e80-02b8-11eb-89ca-e5545f70feb2.png)  | ![Screenshot_2019-12-30-04-45-01-374_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94597949-7380f000-02b8-11eb-8399-bbcfb29102fb.png) |

Рисунок 6 – Установка параметров профилировщика, отображение оцениваемых задач в общем списке и результат работы профайлера

| ![Screenshot_2019-12-30-04-26-18-273_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94597947-72e85980-02b8-11eb-9400-c9c8c63b4cdb.png)  | ![Screenshot_2019-12-30-04-26-44-056_com hpccloud ssd sscc](https://user-images.githubusercontent.com/25662601/94597948-7380f000-02b8-11eb-905e-988d18a56d6f.png) |

Рисунок 7 – Всплывающие сообщения профилировщика
