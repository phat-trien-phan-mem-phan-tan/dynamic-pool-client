package vn.edu.hust.student.dynamicpool.events;

import java.lang.reflect.InvocationTargetException;

import com.eposi.eventdriven.Event;
import com.eposi.eventdriven.exceptions.InvalidHandlerMethod;
import com.eposi.eventdriven.exceptions.NoContextToExecute;
import com.eposi.eventdriven.implementors.BaseEventDispatcher;
import com.eposi.eventdriven.implementors.BaseEventListener;

public class EventDestination {
	private BaseEventDispatcher eventDispatcher = new BaseEventDispatcher();
	private static EventDestination _instance = null;

	public static EventDestination getInstance() {
		if (_instance == null)
			_instance = new EventDestination();
		return _instance;
	}

	public EventDestination() {

	}

	public void addEventListener(EventType eventType,
			BaseEventListener baseEventListener) {
		eventDispatcher.addEventListener(eventType.toString(),
				baseEventListener);
	}

	public void dispatchSuccessEvent(EventType eventType) {
		dispatchEvent(eventType, true, null, null);
	}

	private void dispatchEvent(EventType eventType, boolean isSuccess,
			Object targetObject, Exception error) {
		EventResult eventResult = new EventResult(isSuccess, targetObject,
				error);
		try {
			eventDispatcher.dispatchEvent(new Event(eventType.toString(),
					eventResult));
		} catch (InvocationTargetException e) {
			System.err.println(EventDestination.class.toString() + " "
					+ e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println(EventDestination.class.toString() + " "
					+ e.getMessage());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.err.println(EventDestination.class.toString() + " "
					+ e.getMessage());
			e.printStackTrace();
		} catch (InvalidHandlerMethod e) {
			System.err.println(EventDestination.class.toString() + " "
					+ e.getMessage());
			e.printStackTrace();
		} catch (NoContextToExecute e) {
			System.err.println(EventDestination.class.toString() + " "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void dispatchFailEvent(EventType eventType) {
		dispatchEvent(eventType, false, null, null);
	}

	public void dispatchSuccessEventWithObject(EventType eventType,
			Object targetObject) {
		dispatchEvent(eventType, true, targetObject, null);
	}

	public void dispatchFailEventWithExeption(EventType eventType,
			Exception error) {
		dispatchEvent(eventType, false, null, error);
	}

	public void dispatchFailEventWithObject(EventType eventType,
			Exception error, Object targetObject) {
		dispatchEvent(eventType, false, targetObject, error);
	}

	public static boolean parseEventToBoolean(Event event) {
		if (EventResult.class.isInstance(event.getTarget())) {
			return ((EventResult) event.getTarget()).isSuccess();
		}
		try {
			boolean isSuccess = Boolean.parseBoolean(event.getTarget().toString());
			return isSuccess;
		} catch (Exception e) {
			return false;
		}
	}

	public static Object parseEventToTargetObject(Event event) {
		if (EventResult.class.isInstance(event.getTarget())) {
			return ((EventResult) event.getTarget()).getTargetObject();
		}
		return event.getTarget();
	}
}