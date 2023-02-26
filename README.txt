Elevator Simulator Project - Iteration 1

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
Iteration 1: Establish Connections between the three subsystems (current version)
 - Development for the three threads of Floor, Elevator and Scheduler.
Iteration 2: Adding the scheduler and Elevator Subsystem
Iteration 3: Multiple Cars and System Distribution
Iteration 4: Adding Error detection and correction
Iteration 5: Measuring the Scheduler and predicting the performance

Design:
For better understanding the design of the project please navigate to the "./diagrams" folder.
- The UML class diagram for this iteration is located in "./diagrams/class_diagrams" labelled "class_diagram_iter1.png".
- The sequence diagrams are located in "./diagrams/sequence_diagrams/Iteration1" as follows:
	- "elevator_to_floor.png"
	- "floor_to_elevator.png"

Contents of src:
- Package ElevatorSimulator
	- Buffer.java : An implementation of a simple blocking queue.
	- Elevator.java : The elevator subsystem.
	- Floor.java : The floor subsystem.
	- Scheduler.java : The scheduler subsystem.
	- Simulator.java : Starting point for the program.
- Package ElevatorSimulator.Messages
	- ArrivedElevatorMessage.java : Message for elevator arriving.
	- DirectionType.java : Enum for the direction of travel of the elevator.
	- KillMessage.java : Specific message for killing the system.
	- Message.java : The default messages that are passed in the buffers.
	- MessageType.java : Enum for message types.
	- RequestElevatorMessage.java : Message for elevator requests.
	- SenderType.java : Enum for types of senders.
- Package ElevatorSimulator.resources
	- elevator_input.csv : csv file containing commands that the simulator takes in.
- Package ElevatorSimulatorTest
	- BufferTest.java : Tests for the buffer class.
	- ElevatorTest.java : Tests for the elevator subsystem.
	- FloorTest.java : Tests for the floor subsystem.
	- MockSheduler.java : A mock version of the Scheduler class for testing purposes.
	- SchedulerTest.java : Tests for the scheduler system.
	 - SimulatorTest.java : Tests for the simulator.
- Package ElevatorSimulatorTest.TestFiles
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

Setup Instructions:
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

To help isolate the floor and elevator subsystems from each other, the scheduler subsystem was mocked and labeled as "MockScheduler".
This allows the scheduler to behave synchronously and avoid having the any of the tests be blocked waiting for the response of the other subsystem.

Navigate to any of these class and run them as a JUnit test to confirm that the system is working as expected. 

Expected Output:
Expected output of the simulator after running the project with the default test file "elevator_input.csv":
'''
FLOOR sent
REQUEST 14:33:44.5 FLOOR
from: 2 to: 4 UP

SCHEDULER received
REQUEST 14:33:44.5 FLOOR
from: 2 to: 4 UP

SCHEDULER sent
REQUEST 14:33:44.5 FLOOR
from: 2 to: 4 UP

ELEVATOR received
REQUEST 14:33:44.5 FLOOR
from: 2 to: 4 UP

ELEVATOR sent
ARRIVE 14:33:44.5 ELEVATOR
at: 4

SCHEDULER received
ARRIVE 14:33:44.5 ELEVATOR
at: 4

SCHEDULER sent
ARRIVE 14:33:44.5 ELEVATOR
at: 4

FLOOR received
ARRIVE 14:33:44.5 ELEVATOR
at: 4

FLOOR sent
REQUEST 16:42:21.2 FLOOR
from: 4 to: 3 DOWN

SCHEDULER received
REQUEST 16:42:21.2 FLOOR
from: 4 to: 3 DOWN

SCHEDULER sent
REQUEST 16:42:21.2 FLOOR
from: 4 to: 3 DOWN

ELEVATOR received
REQUEST 16:42:21.2 FLOOR
from: 4 to: 3 DOWN

ELEVATOR sent
ARRIVE 16:42:21.2 ELEVATOR
at: 3

SCHEDULER received
ARRIVE 16:42:21.2 ELEVATOR
at: 3

SCHEDULER sent
ARRIVE 16:42:21.2 ELEVATOR
at: 3

FLOOR received
ARRIVE 16:42:21.2 ELEVATOR
at: 3

FLOOR sent
REQUEST 24:11:07.0 FLOOR
from: 3 to: 1 DOWN

SCHEDULER received
REQUEST 24:11:07.0 FLOOR
from: 3 to: 1 DOWN

SCHEDULER sent
REQUEST 24:11:07.0 FLOOR
from: 3 to: 1 DOWN

ELEVATOR received
REQUEST 24:11:07.0 FLOOR
from: 3 to: 1 DOWN

ELEVATOR sent
ARRIVE 24:11:07.0 ELEVATOR
at: 1

SCHEDULER received
ARRIVE 24:11:07.0 ELEVATOR
at: 1

SCHEDULER sent
ARRIVE 24:11:07.0 ELEVATOR
at: 1

FLOOR received
ARRIVE 24:11:07.0 ELEVATOR
at: 1

FLOOR sent
REQUEST 00:32:14.8 FLOOR
from: 1 to: 3 UP

SCHEDULER received
REQUEST 00:32:14.8 FLOOR
from: 1 to: 3 UP

SCHEDULER sent
REQUEST 00:32:14.8 FLOOR
from: 1 to: 3 UP

ELEVATOR received
REQUEST 00:32:14.8 FLOOR
from: 1 to: 3 UP

ELEVATOR sent
ARRIVE 00:32:14.8 ELEVATOR
at: 3

SCHEDULER received
ARRIVE 00:32:14.8 ELEVATOR
at: 3

SCHEDULER sent
ARRIVE 00:32:14.8 ELEVATOR
at: 3

FLOOR received
ARRIVE 00:32:14.8 ELEVATOR
at: 3

FLOOR sent
REQUEST 01:29:00.6 FLOOR
from: 3 to: 4 UP

SCHEDULER received
REQUEST 01:29:00.6 FLOOR
from: 3 to: 4 UP

SCHEDULER sent
REQUEST 01:29:00.6 FLOOR
from: 3 to: 4 UP

ELEVATOR received
REQUEST 01:29:00.6 FLOOR
from: 3 to: 4 UP

ELEVATOR sent
ARRIVE 01:29:00.6 ELEVATOR
at: 4

SCHEDULER received
ARRIVE 01:29:00.6 ELEVATOR
at: 4

SCHEDULER sent
ARRIVE 01:29:00.6 ELEVATOR
at: 4

FLOOR received
ARRIVE 01:29:00.6 ELEVATOR
at: 4

FLOOR sent
KILL 00:00:00 FLOOR

No more floor requests remaining

SCHEDULER received
KILL 00:00:00 FLOOR

No more floor requests remaining

SCHEDULER sent
KILL 00:00:00 FLOOR

No more floor requests remaining

ELEVATOR received
KILL 00:00:00 FLOOR

No more floor requests remaining
'''



