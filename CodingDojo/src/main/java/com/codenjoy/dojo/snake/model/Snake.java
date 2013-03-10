package com.codenjoy.dojo.snake.model;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.snake.model.artifacts.Element;
import com.codenjoy.dojo.snake.model.artifacts.Point;

public class Snake implements Element, Iterable<Point>, Joystick {

	private LinkedList<Point> elements;
	private Direction direction; 
	private boolean alive;
	private int growBy;

	public Snake(int x, int y) {	
		elements = new LinkedList<Point>();
		elements.addFirst(new Point(x, y));
		elements.addFirst(new Point(x - 1, y));
		
		growBy = 0;
				
		direction = Direction.RIGHT;
		alive = true;
	}
	
	public int getX() {
		return getHead().getX();
	}

	public int getY() {
		return getHead().getY();
	}

	public int getLength() {
		return elements.size();
	}

	public Direction getDirection() {
		return direction;
	}

	public void move(int x, int y) {
		elements.addLast(new Point(x, y));
		
		if (growBy < 0) { 			
			for (int count = 0; count <= -growBy; count++) {
				elements.removeFirst();
			}
		} else if (growBy > 0) {
			
		} else { // == 0
			elements.removeFirst();
		}
		growBy = 0;		
	}

    @Override
	public void down() {
		checkAlive();
		direction = Direction.DOWN;
	}

    @Override
	public void up() {
		checkAlive();
		direction = Direction.UP;		
	}

    @Override
	public void left() {
		checkAlive();
		direction = Direction.LEFT;		
	}

    @Override
	public void right() {
		checkAlive();
		direction = Direction.RIGHT;
	}

    @Override
    public void act() {

    }

    void checkAlive() {
		if (!isAlive()) {
			throw new IllegalStateException("Game over!");
		}
	}
	
	public boolean isAlive() {
		return alive;
	}

	public void killMe() {
		alive = false;
	}

	public void grow() {
		growBy = 1;
	}

	public boolean itsMyHead(Point point) {
		return (getHead().itsMe(point));
	}
	
	public boolean itsMe(Point point) {
		return itsMyBody(point) || itsMyHead(point);
	}

    public boolean itsMe(int x, int y) {
        return itsMe(new Point(x, y));
    }
	
	public boolean itsMyBody(Point point) {		
		if (itsMyHead(point)) {
			return false;
		}
		
		for (Point element : elements) {
			if (element.itsMe(point)) {
				return true;
			}
		}
		return false;
	}

	public Point getHead() {
		return elements.getLast();
	}

	@Override
	public void affect(Snake snake) {
		killMe();
	}

	public void walk(Board board) {
		Point place = whereToMove();								
		place = teleport(board.getSize(), place);
		board.getAt(place).affect(this);
        validatePosition(board.getSize(), place);
        move(place.getX(), place.getY());
	}

    private void validatePosition(int boardSize, Point place) {
        if (place.getX() >= boardSize || place.getX() < 0 ||
            place.getY() >= boardSize || place.getY() < 0)
        {
            this.killMe();
        }
    }

    private Point teleport(int boardSize, Point point) {
        int x = point.getX();
        int y = point.getY();
        if (x == boardSize) {
            x = 0;
        } else if (x == -1) {
            x = boardSize - 1;
        }
        if (y == boardSize) {
            y = 0;
        } else if (y == -1) {
            y = boardSize - 1;
        }

        return new Point(x, y);
    }

    private Point whereToMove() {
		int x = getX();
		int y = getY();
		if (Direction.RIGHT.equals(direction)) {
			x++;
		} else if (Direction.UP.equals(direction)) {
			y++;
		} else if (Direction.DOWN.equals(direction))  {					
			y--;
		} else {			
			x--;
		}
		return new Point(x, y);
	}

	public boolean itsMyTail(Point point) {
		return getTail().itsMe(point);
	}

    public Point getTail() {
        return elements.getFirst();
    }

    @Override
	public Iterator<Point> iterator() {
		return elements.iterator();
	}

	public void eatStone() {
		if (elements.size() <= 10) {
			killMe();
		} else {
			growBy = -10;
		}		
	}

    public BodyDirection getBodyDirection(Point curr) {
        int index = elements.indexOf(curr);
        Point prev = elements.get(index);
        Point next = elements.get(index);

        if (next.getX() == prev.getX()) {
//            return BodyDirection.HORIZONTAL;
        } else if (next.getY() == prev.getY()) {
//            return BodyDirection.VERTICAL;
        } else {
//            if (isLeftDown(curr, prev, next) || isLeftDown(curr, next, prev)) {
//                return BodyDirection.TURNED_LEFT_DOWN;
//            } else if (isLeftUp(curr, prev, next) || isLeftDown(curr, next, prev)) {
//                return BodyDirection.TURNED_LEFT_UP;
//            } else if (isRightDown(curr, prev, next) || isLeftDown(curr, next, prev)) {
//                return BodyDirection.TURNED_LEFT_DOWN;
//            } else {
//
//            }
        }
        return null;
    }

//    private boolean isLeftDown(Point curr, Point prev, Point next) {
//        return next.getX() == curr.getX() && next.getX() < prev.getX();
//    }

    public Direction getTailDirection() {
        Point prev = elements.get(1);
        Point tail = getTail();

        if (prev.getX() == tail.getX()) {
            if (prev.getY() < tail.getY()) {
                return Direction.UP;
            } else {
                return Direction.DOWN;
            }
        } else {
            if (prev.getX() < tail.getX()) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        }
    }

}
