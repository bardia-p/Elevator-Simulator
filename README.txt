Elevator Simulator Project - Iteration 2

Authors (Group L1G3)
 - Andre Hazim - 101141843
 - Bardia Parmoun - 101143006
 - Guy Morgenshtern - 101151430
 - Kyra Lothrop - 101145872
 - Sarah Chow - 101143033

Description:
The purpose of this project is to simulate the behaviour of an elevator system in real time. 
The Elevator Simulator is composed of the Scheduler, Elevator, and Floor subsystems, that are constantly interacting with each other.

Deliverables:
Iteration 0: Measure a Real Elevator (completed)
Iteration 1: Establish Connections between the three subsystems (completed)
Iteration 2: Adding the scheduler and Elevator Subsystem (current version)
 - Addition of state machines for the scheduler and elevator subsystems.
Iteration 3: Multiple Cars and System Distribution
Iteration 4: Adding Error detection and correction
Iteration 5: Measuring the Scheduler and predicting the performance

Design:
For better understanding the design of the project please navigate to the "./diagrams" folder.
 - The UML class diagram for this iteration is located in "./diagrams/class_diagrams" labelled "class_diagram_iter2.png".
 - The sequence diagrams are located in "./diagrams/sequence_diagrams/Iteration2" as follows:
	- "elevator_to_floor.png"
	- "floor_to_elevator.png"
 - The state diagrams are located in "./diagrams/state_diagrams" as follows:
      - "elevator_state_machine.jpg"
      - "scheduler_state_machine.jpg"

Contents:
 - Package ElevatorSimulator
      - Simulator.java : The class in charge of starting up the subsystems.
 - Package ElevatorSimulator.Elevator
      - Elevator.java : The elevator subsystem, receives and replies to messages.
      - ElevatorController.java : Responsible for controlling the multiple elevators.
      - ElevatorState.java : Enum for The possible states of the elevator
 - Package ElevatorSimulator.Floor
      - Floor.java : The floor subsystem.
 - Package ElevatorSimulator.Messages
      - ArrivedElevatorMessage.java : Message to indicate the elevator has arrived.
      - DirectionType.java : Enum for the possible directions of the elevator.
      - DoorOpenedMessage.java : Message to indicate that the doors have opened.
      - KillMessage.java : Message used to kill the system.
      - Message.java : The default message class for holding the information that is passed to the buffers.
      - MessageType.java : Enum for the possible message types passed in the buffer. 
      - RequestElevatorMessage.java : Message for requesting the elevator.
      - SenderType.java : Enum for the subsystems that can send messages.
      - StopType.java : Enum for the possible reasons that the doors have opened.
 - Package ElevatorSimulator.Messaging
      - Buffer.java : A implementation of a simple blocking queue.
      - MessageQueue.java : File representing the MessageQueue class.
 - Package ElevatorSimulator.Scheduler
      - Scheduler.java : The scheduler subsystem that removes a message from the elevator and sends it to the floor and removes a message from the floor and sends it to the elevator.
      - SchedulerState.java : Enum for the possible scheduler states.
 - Package ElevatorSimulator.resources
      - elevator_input.csv : csv file containing commands.
 - Package ElevatorSimulatorTest
      - BufferTest.java : The unit tests for the buffer class.
      - ElevatorTest.java : The unit tests for the elevator subsystem.
      - FloorTest.java : The unit tests for the floor subsystem.
      - MockElevatorController.java : Class to mock the ElevatorController class for testing purposes.
      - SchedulerTest.java : The unit tests for the scheduler subsystem using the mock ElevatorController class.
      - SimulatorTest.java : File to test the Simulator class and tests the completion of the system using the default input file and the test file.
 - Package ElevatorSimulator.TestFiles
      - elevator_test-1.csv : csv file with commands for testing purposes.


* Note: if you notice any ".DS_STORE" files, they are automatically created and are related to the internal file structure of macOS. 

Individual Contributions: 
Iteration 1:
 - Andre Hazim: Elevator and Scheduler subsystem + testing
	- ElevatorSimulator: Elevator.java, Scheduler.java, ArrivedElevatorMessage.java
	- ElevatorSimulatorTest: SimulatorTest.java
 - Bardia Parmoun: Scheduler subsystem + testing
	- ElevatorSimulator: Buffer.java, Scheduler.java, Message.java
	- ElevatorSimulatorTest: ElevatorTest.java, FloorTest.java, MockScheduler.java, SchedulerTest.java, SimulatorTest.java
 - Guy Morgenshtern: Floor subsystem + testing
	- ElevatorSimulator: Floor.java, Simulator.java, Message.java, KillMessage.java, RequestElevatorMessage.java
	- ElevatorSimulatorTest: BufferTest.java
 - Kyra Lothrop: Elevator subsystem + testing
	- ElevatorSimulator: Buffer.java, Elevator.java, ArrivedElevatorMessage.java, KillMessage.java
	- ElevatorSimulatorTest: BufferTest.java
 - Sarah Chow: Floor subsystem + testing
	- ElevatorSimulator: Floor.java, Simulator.java, RequestElevatorMessage.java
	- ElevatorSimulatorTest: ElevatorTest.java, FloorTest.java, MockScheduler.java, SchedulerTest.java

Iteration 2:
 - Andre Hazim:
	- ElevatorSimulator: Elevator.java, OpenDoorsMessage.java, MessageQueue.java
 - Bardia Parmoun:
	- ElevatorSimulator: MessageQueue.java, Scheduler.java, ElevatorController.java
	- ElevatorSimulatorTest: ElevatorTest.java
 - Guy Morgenshtern:
	- ElevatorSimulator: Elevator.java, OpenDoorsMessage.java, MessageQueue.java
 - Kyra Lothrop:
	- ElevatorSimulator: MessageQueue.java, Scheduler.java, ElevatorController.java
	- ElevatorSimulatorTest: FloorTest.java, SchedulerTest.java
 - Sarah Chow:
	- ElevatorSimulator: MessageQueue.java, Scheduler.java, ElevatorController.java
	- ElevatorSimulatorTest: ElevatorTest.java, FloorTest.java, SchedulerTest.java

Setup instructions:
1. Unzip the submission file.
2. Navigate the eclipse IDE.
3. Navigate the File menu.
4. Open the "Open Project from File System.." option.
5. Select the root folder for the project "ElevatorSimulator".
6. Click the "Finish button".
7. Select the project folder. 
8. Once the project folder is open navigate to the "./src/ElevatorSimulator/Simulator.java" file to run the main file and start the program.

Test Instructions:
The ElevatorSimulator.Test package has been dedicated to testing. The testing framework that was used in this project is JUnit 5.

For each subsystem there is a dedicated test class to test them separately.
- FloorTest: testing the floor subsystem alone.
- ElevatorTest: testing the elevator subsystem alone.
- SchedulerTest: testing the scheduler subsystem alone.
- BufferTest: testing the blocking message queue.
- SimulatorTest: testing the simulator system.

The ElevatorController was mocked and labeled as "MockElevatorController".
This allows the ElevatorController to behave synchronously and avoid blocking the tests.

Navigate to any of these classes and run them as a JUnit test to confirm that the system is
working as expected. 

Expected Output:
Expected output of the simulator after running the project with the default test file "elevator_input.csv":
'''
SCHEDULER STATE: --------- POLL ---------

------------------------
FLOOR sent: REQUEST 14:33:44.5 FLOOR from: 2 to: 4 UP
------------------------


------------------------
SCHEDULER received: REQUEST 14:33:44.5 FLOOR from: 2 to: 4 UP
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

FLOOR LIGHTS STATUS
---------------------------------------
| Floor 1 up light on :false  down light on :false |
| Floor 2 up light on :true  down light on :false |
| Floor 3 up light on :false  down light on :false |
| Floor 4 up light on :false  down light on :false |
---------------------------------------


ELEVATOR STATE: --------- POLL ---------

------------------------
SCHEDULER sent: REQUEST 14:33:44.5 FLOOR from: 2 to: 4 UP
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
ELEVATOR 1 received: REQUEST 14:33:44.5 FLOOR from: 2 to: 4 UP
------------------------


ELEVATOR STATE: --------- MOVING ---------

ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 14:33:44.5 ELEVATOR at: 2
------------------------


------------------------
SCHEDULER received: ARRIVE 14:33:44.5 ELEVATOR at: 2
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

SCHEDULER STATE: --------- POLL ---------

ELEVATOR STATE: --------- OPEN ---------

------------------------
ELEVATOR 1 sent: DOORS_OPENED 14:33:44.5 ELEVATOR Stop Type: PICKUP
------------------------


------------------------
SCHEDULER received: DOORS_OPENED 14:33:44.5 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
SCHEDULER sent: DOORS_OPENED 14:33:44.5 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
FLOOR received: DOORS_OPENED 14:33:44.5 ELEVATOR Stop Type: PICKUP
------------------------


FLOOR LIGHTS STATUS
---------------------------------------
| Floor 1 up light on :false  down light on :false |
| Floor 2 up light on :false  down light on :false |
| Floor 3 up light on :false  down light on :false |
| Floor 4 up light on :false  down light on :false |
---------------------------------------


ELEVATOR STATE: --------- BOARDING ---------

ELEVATOR STATE: --------- CLOSE ---------

ELEVATOR LIGHTS STATUS
------------------------------------------------
| Floor 1 light on: false |
| Floor 2 light on: false |
| Floor 3 light on: false |
| Floor 4 light on: true |
------------------------------------------------


ELEVATOR STATE: --------- POLL ---------

ELEVATOR STATE: --------- MOVING ---------

ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 14:33:44.5 ELEVATOR at: 3
------------------------


ELEVATOR STATE: --------- POLL ---------

ELEVATOR STATE: --------- MOVING ---------

------------------------
SCHEDULER received: ARRIVE 14:33:44.5 ELEVATOR at: 3
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

SCHEDULER STATE: --------- POLL ---------

ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 14:33:44.5 ELEVATOR at: 4
------------------------


ELEVATOR STATE: --------- OPEN ---------

------------------------
SCHEDULER received: ARRIVE 14:33:44.5 ELEVATOR at: 4
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

SCHEDULER STATE: --------- POLL ---------

------------------------
ELEVATOR 1 sent: DOORS_OPENED 14:33:44.5 ELEVATOR Stop Type: DROPOFF
------------------------


------------------------
SCHEDULER received: DOORS_OPENED 14:33:44.5 ELEVATOR Stop Type: DROPOFF
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
SCHEDULER sent: DOORS_OPENED 14:33:44.5 ELEVATOR Stop Type: DROPOFF
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
FLOOR received: DOORS_OPENED 14:33:44.5 ELEVATOR Stop Type: DROPOFF
------------------------


------------------------
FLOOR sent: REQUEST 16:42:21.2 FLOOR from: 4 to: 3 DOWN
------------------------


------------------------
SCHEDULER received: REQUEST 16:42:21.2 FLOOR from: 4 to: 3 DOWN
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

FLOOR LIGHTS STATUS
---------------------------------------
| Floor 1 up light on :false  down light on :false |
| Floor 2 up light on :false  down light on :false |
| Floor 3 up light on :false  down light on :false |
| Floor 4 up light on :false  down light on :true |
---------------------------------------


ELEVATOR STATE: --------- BOARDING ---------

ELEVATOR STATE: --------- CLOSE ---------

ELEVATOR LIGHTS STATUS
------------------------------------------------
| Floor 1 light on: false |
| Floor 2 light on: false |
| Floor 3 light on: false |
| Floor 4 light on: false |
------------------------------------------------


ELEVATOR STATE: --------- POLL ---------

------------------------
SCHEDULER sent: REQUEST 16:42:21.2 FLOOR from: 4 to: 3 DOWN
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
ELEVATOR 1 received: REQUEST 16:42:21.2 FLOOR from: 4 to: 3 DOWN
------------------------


ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 16:42:21.2 ELEVATOR at: 4
------------------------


ELEVATOR STATE: --------- OPEN ---------

------------------------
SCHEDULER received: ARRIVE 16:42:21.2 ELEVATOR at: 4
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

SCHEDULER STATE: --------- POLL ---------

------------------------
ELEVATOR 1 sent: DOORS_OPENED 16:42:21.2 ELEVATOR Stop Type: PICKUP
------------------------


------------------------
SCHEDULER received: DOORS_OPENED 16:42:21.2 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
SCHEDULER sent: DOORS_OPENED 16:42:21.2 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
FLOOR received: DOORS_OPENED 16:42:21.2 ELEVATOR Stop Type: PICKUP
------------------------


FLOOR LIGHTS STATUS
---------------------------------------
| Floor 1 up light on :false  down light on :false |
| Floor 2 up light on :false  down light on :false |
| Floor 3 up light on :false  down light on :false |
| Floor 4 up light on :false  down light on :true |
---------------------------------------


ELEVATOR STATE: --------- BOARDING ---------

ELEVATOR STATE: --------- CLOSE ---------

ELEVATOR LIGHTS STATUS
------------------------------------------------
| Floor 1 light on: false |
| Floor 2 light on: false |
| Floor 3 light on: true |
| Floor 4 light on: false |
------------------------------------------------


ELEVATOR STATE: --------- POLL ---------

ELEVATOR STATE: --------- MOVING ---------

ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 16:42:21.2 ELEVATOR at: 3
------------------------


ELEVATOR STATE: --------- OPEN ---------

------------------------
SCHEDULER received: ARRIVE 16:42:21.2 ELEVATOR at: 3
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

SCHEDULER STATE: --------- POLL ---------

------------------------
ELEVATOR 1 sent: DOORS_OPENED 16:42:21.2 ELEVATOR Stop Type: DROPOFF
------------------------


------------------------
SCHEDULER received: DOORS_OPENED 16:42:21.2 ELEVATOR Stop Type: DROPOFF
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
SCHEDULER sent: DOORS_OPENED 16:42:21.2 ELEVATOR Stop Type: DROPOFF
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
FLOOR received: DOORS_OPENED 16:42:21.2 ELEVATOR Stop Type: DROPOFF
------------------------


------------------------
FLOOR sent: REQUEST 24:11:07.0 FLOOR from: 3 to: 1 DOWN
------------------------


------------------------
SCHEDULER received: REQUEST 24:11:07.0 FLOOR from: 3 to: 1 DOWN
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

FLOOR LIGHTS STATUS
---------------------------------------
| Floor 1 up light on :false  down light on :false |
| Floor 2 up light on :false  down light on :false |
| Floor 3 up light on :false  down light on :true |
| Floor 4 up light on :false  down light on :true |
---------------------------------------


ELEVATOR STATE: --------- BOARDING ---------

ELEVATOR STATE: --------- CLOSE ---------

ELEVATOR LIGHTS STATUS
------------------------------------------------
| Floor 1 light on: false |
| Floor 2 light on: false |
| Floor 3 light on: false |
| Floor 4 light on: false |
------------------------------------------------


ELEVATOR STATE: --------- POLL ---------

------------------------
SCHEDULER sent: REQUEST 24:11:07.0 FLOOR from: 3 to: 1 DOWN
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
ELEVATOR 1 received: REQUEST 24:11:07.0 FLOOR from: 3 to: 1 DOWN
------------------------


ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 24:11:07.0 ELEVATOR at: 3
------------------------


ELEVATOR STATE: --------- OPEN ---------

------------------------
SCHEDULER received: ARRIVE 24:11:07.0 ELEVATOR at: 3
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
ELEVATOR 1 sent: DOORS_OPENED 24:11:07.0 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
SCHEDULER received: DOORS_OPENED 24:11:07.0 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
SCHEDULER sent: DOORS_OPENED 24:11:07.0 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
FLOOR received: DOORS_OPENED 24:11:07.0 ELEVATOR Stop Type: PICKUP
------------------------


FLOOR LIGHTS STATUS
---------------------------------------
| Floor 1 up light on :false  down light on :false |
| Floor 2 up light on :false  down light on :false |
| Floor 3 up light on :false  down light on :false |
| Floor 4 up light on :false  down light on :true |
---------------------------------------


ELEVATOR STATE: --------- BOARDING ---------

ELEVATOR STATE: --------- CLOSE ---------

ELEVATOR LIGHTS STATUS
------------------------------------------------
| Floor 1 light on: true |
| Floor 2 light on: false |
| Floor 3 light on: false |
| Floor 4 light on: false |
------------------------------------------------


ELEVATOR STATE: --------- POLL ---------

ELEVATOR STATE: --------- MOVING ---------

ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 24:11:07.0 ELEVATOR at: 2
------------------------


------------------------
SCHEDULER received: ARRIVE 24:11:07.0 ELEVATOR at: 2
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

ELEVATOR STATE: --------- POLL ---------

SCHEDULER STATE: --------- POLL ---------

ELEVATOR STATE: --------- MOVING ---------

ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 24:11:07.0 ELEVATOR at: 1
------------------------


ELEVATOR STATE: --------- OPEN ---------

------------------------
SCHEDULER received: ARRIVE 24:11:07.0 ELEVATOR at: 1
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

SCHEDULER STATE: --------- POLL ---------

------------------------
ELEVATOR 1 sent: DOORS_OPENED 24:11:07.0 ELEVATOR Stop Type: DROPOFF
------------------------


------------------------
SCHEDULER received: DOORS_OPENED 24:11:07.0 ELEVATOR Stop Type: DROPOFF
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
SCHEDULER sent: DOORS_OPENED 24:11:07.0 ELEVATOR Stop Type: DROPOFF
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
FLOOR received: DOORS_OPENED 24:11:07.0 ELEVATOR Stop Type: DROPOFF
------------------------


------------------------
FLOOR sent: REQUEST 00:32:14.8 FLOOR from: 1 to: 3 UP
------------------------


------------------------
SCHEDULER received: REQUEST 00:32:14.8 FLOOR from: 1 to: 3 UP
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

FLOOR LIGHTS STATUS
---------------------------------------
| Floor 1 up light on :true  down light on :false |
| Floor 2 up light on :false  down light on :false |
| Floor 3 up light on :false  down light on :false |
| Floor 4 up light on :false  down light on :true |
---------------------------------------


ELEVATOR STATE: --------- BOARDING ---------

ELEVATOR STATE: --------- CLOSE ---------

ELEVATOR LIGHTS STATUS
------------------------------------------------
| Floor 1 light on: false |
| Floor 2 light on: false |
| Floor 3 light on: false |
| Floor 4 light on: false |
------------------------------------------------


ELEVATOR STATE: --------- POLL ---------

------------------------
SCHEDULER sent: REQUEST 00:32:14.8 FLOOR from: 1 to: 3 UP
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
ELEVATOR 1 received: REQUEST 00:32:14.8 FLOOR from: 1 to: 3 UP
------------------------


ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 00:32:14.8 ELEVATOR at: 1
------------------------


ELEVATOR STATE: --------- OPEN ---------

------------------------
SCHEDULER received: ARRIVE 00:32:14.8 ELEVATOR at: 1
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
ELEVATOR 1 sent: DOORS_OPENED 00:32:14.8 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
SCHEDULER received: DOORS_OPENED 00:32:14.8 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
SCHEDULER sent: DOORS_OPENED 00:32:14.8 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
FLOOR received: DOORS_OPENED 00:32:14.8 ELEVATOR Stop Type: PICKUP
------------------------


FLOOR LIGHTS STATUS
---------------------------------------
| Floor 1 up light on :true  down light on :false |
| Floor 2 up light on :false  down light on :false |
| Floor 3 up light on :false  down light on :false |
| Floor 4 up light on :false  down light on :true |
---------------------------------------


ELEVATOR STATE: --------- BOARDING ---------

ELEVATOR STATE: --------- CLOSE ---------

ELEVATOR LIGHTS STATUS
------------------------------------------------
| Floor 1 light on: false |
| Floor 2 light on: false |
| Floor 3 light on: true |
| Floor 4 light on: false |
------------------------------------------------


ELEVATOR STATE: --------- POLL ---------

ELEVATOR STATE: --------- MOVING ---------

ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 00:32:14.8 ELEVATOR at: 2
------------------------


ELEVATOR STATE: --------- POLL ---------

------------------------
SCHEDULER received: ARRIVE 00:32:14.8 ELEVATOR at: 2
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

SCHEDULER STATE: --------- POLL ---------

ELEVATOR STATE: --------- MOVING ---------

ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 00:32:14.8 ELEVATOR at: 3
------------------------


------------------------
SCHEDULER received: ARRIVE 00:32:14.8 ELEVATOR at: 3
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

SCHEDULER STATE: --------- POLL ---------

ELEVATOR STATE: --------- OPEN ---------

------------------------
ELEVATOR 1 sent: DOORS_OPENED 00:32:14.8 ELEVATOR Stop Type: DROPOFF
------------------------


------------------------
SCHEDULER received: DOORS_OPENED 00:32:14.8 ELEVATOR Stop Type: DROPOFF
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
SCHEDULER sent: DOORS_OPENED 00:32:14.8 ELEVATOR Stop Type: DROPOFF
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
FLOOR received: DOORS_OPENED 00:32:14.8 ELEVATOR Stop Type: DROPOFF
------------------------


------------------------
FLOOR sent: REQUEST 01:29:00.6 FLOOR from: 3 to: 4 UP
------------------------


FLOOR LIGHTS STATUS
---------------------------------------
| Floor 1 up light on :true  down light on :false |
| Floor 2 up light on :false  down light on :false |
| Floor 3 up light on :true  down light on :false |
| Floor 4 up light on :false  down light on :true |
---------------------------------------


------------------------
SCHEDULER received: REQUEST 01:29:00.6 FLOOR from: 3 to: 4 UP
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

ELEVATOR STATE: --------- BOARDING ---------

ELEVATOR STATE: --------- CLOSE ---------

ELEVATOR LIGHTS STATUS
------------------------------------------------
| Floor 1 light on: false |
| Floor 2 light on: false |
| Floor 3 light on: false |
| Floor 4 light on: false |
------------------------------------------------


ELEVATOR STATE: --------- POLL ---------

------------------------
SCHEDULER sent: REQUEST 01:29:00.6 FLOOR from: 3 to: 4 UP
------------------------


------------------------
ELEVATOR 1 received: REQUEST 01:29:00.6 FLOOR from: 3 to: 4 UP
------------------------


ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 01:29:00.6 ELEVATOR at: 3
------------------------


ELEVATOR STATE: --------- OPEN ---------

SCHEDULER STATE: --------- POLL ---------

------------------------
SCHEDULER received: ARRIVE 01:29:00.6 ELEVATOR at: 3
------------------------


------------------------
ELEVATOR 1 sent: DOORS_OPENED 01:29:00.6 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

SCHEDULER STATE: --------- POLL ---------

------------------------
SCHEDULER received: DOORS_OPENED 01:29:00.6 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
SCHEDULER sent: DOORS_OPENED 01:29:00.6 ELEVATOR Stop Type: PICKUP
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
FLOOR received: DOORS_OPENED 01:29:00.6 ELEVATOR Stop Type: PICKUP
------------------------


FLOOR LIGHTS STATUS
---------------------------------------
| Floor 1 up light on :true  down light on :false |
| Floor 2 up light on :false  down light on :false |
| Floor 3 up light on :false  down light on :false |
| Floor 4 up light on :false  down light on :true |
---------------------------------------


ELEVATOR STATE: --------- BOARDING ---------

ELEVATOR STATE: --------- CLOSE ---------

ELEVATOR LIGHTS STATUS
------------------------------------------------
| Floor 1 light on: false |
| Floor 2 light on: false |
| Floor 3 light on: false |
| Floor 4 light on: true |
------------------------------------------------


ELEVATOR STATE: --------- POLL ---------

ELEVATOR STATE: --------- MOVING ---------

ELEVATOR STATE: --------- ARRIVED ---------

------------------------
ELEVATOR 1 sent: ARRIVE 01:29:00.6 ELEVATOR at: 4
------------------------


ELEVATOR STATE: --------- OPEN ---------

------------------------
SCHEDULER received: ARRIVE 01:29:00.6 ELEVATOR at: 4
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

SCHEDULER STATE: --------- POLL ---------

------------------------
ELEVATOR 1 sent: DOORS_OPENED 01:29:00.6 ELEVATOR Stop Type: DROPOFF
------------------------


------------------------
SCHEDULER received: DOORS_OPENED 01:29:00.6 ELEVATOR Stop Type: DROPOFF
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
SCHEDULER sent: DOORS_OPENED 01:29:00.6 ELEVATOR Stop Type: DROPOFF
------------------------


SCHEDULER STATE: --------- POLL ---------

------------------------
FLOOR received: DOORS_OPENED 01:29:00.6 ELEVATOR Stop Type: DROPOFF
------------------------


------------------------
FLOOR sent: KILL 00:00:00 FLOOR 
No more floor requests remaining
------------------------


------------------------
SCHEDULER received: KILL 00:00:00 FLOOR 
No more floor requests remaining
------------------------


SCHEDULER STATE: --------- PROCESSING ---------

------------------------
SCHEDULER sent: KILL 00:00:00 FLOOR 
No more floor requests remaining
------------------------


SCHEDULER STATE: --------- POLL ---------

ELEVATOR STATE: --------- BOARDING ---------

ELEVATOR STATE: --------- CLOSE ---------

ELEVATOR LIGHTS STATUS
------------------------------------------------
| Floor 1 light on: false |
| Floor 2 light on: false |
| Floor 3 light on: false |
| Floor 4 light on: false |
------------------------------------------------


ELEVATOR STATE: --------- POLL ---------

------------------------
ELEVATOR 1 received: KILL 00:00:00 FLOOR 
No more floor requests remaining
------------------------
'''



