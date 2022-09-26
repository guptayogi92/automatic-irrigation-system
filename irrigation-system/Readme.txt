Automatic Irrigation System:

Overview: This system allows to irrigate plots based on the configuration without human intervention. This system allows the following.
1) Add new plot
2) Edit the plot
3) View available plots for a customer
4) Delete the plot
5) Create new schedule for the plot
6) Modify the existing schedule
7) Delete the schedule


This system use some predefined constants.
crop_name: RICE,WHEAT,MILLETS,PULSES,TEA,COFFEE,SUGARCANE,COTTON,JUTE
soil_type: ALLUVIAL_SOIL,RED_SOIL,BLACK_SOIL,DESERT_SOIL,LATERITE_SOIL,SALINE_SOIL,PEATY_SOIL,FOREST_SOIL,SNOWFIELD_SOIL,SUB_MOUNTAIN_SOIL
schedule_type: REGULAR,CUSTOM
measurement_unit: FOOT,METER
irrugation_day: DAILY,BIWEEKLY,WEEKLY
start_time: HH24:MM format

System allow the REGULAR or CUSTOM schedule for the irrigation. In the CUSTOM schedule_type, system will use the user configured schedule.
For the REGULAR schedule_type, system will use the system pre_configured schedule, This configuration is done in SCHEDULE table.


In the system there is 2 scheduler. Scheduler_1 will run once in a day. This schedular is used to configure the PLOT_IRRIGATION_STATE table.
schedular_2 will run frequently. This schedular will read the data from PLOT_IRRIGATION_STATE table and do the irrigation.

When system fails to communicate with hardware device then this will try upto the max limit. Once the limit reach then it will trigger an email alert.


Prerequisite:
java 11 or higher version


Static code analysis was done on code using sonarlint.


Some important URLs:
H2 DB Console: http://localhost:8080/h2-console
Swagger UI: http://localhost:8080/swagger-ui.html

Inside document folder you will find the postman request collection, DB flow, swagger snapshot

