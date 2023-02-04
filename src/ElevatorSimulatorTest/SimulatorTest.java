package ElevatorSimulatorTest;

import org.junit.jupiter.api.Test;

import ElevatorSimulator.Simulator;

/**
 * @author Bardia Parmoun
 * @author Andre Hazim
 *
 */
public class SimulatorTest {
	/**
	 * Starts the simulator with the default filename.
	 * Validates that the simulator fully runs and does not get stuck.
	 * 
	 */
	@Test
	public void testSimulatorDefaultFile() {		
		Simulator.main(null);
	}
	
	/**
	 * Starts the simulator with a custom filename.
	 * Validates that the simulator fully runs and does not get stuck.
	 * 
	 */
	@Test
	public void testSimulatorTestFile() {
		Simulator.INPUT = "src/ElevatorSimulatorTest/TestFiles/elevator_test-1.csv";
		
		Simulator.main(null);
	}
}
