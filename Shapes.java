
public class Shapes {}

class Rectangle{
	public int x, y, width, height;

	//efficiency for intersection:
	/**top left*/
	public Point a;
	/**top right*/
	public Point b;
	/**bottom left*/
	public Point c;
	/**bottom right*/
	public Point d; 

	public Rectangle(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		//the following will only be instantiated before calculating intersections
		a = new Point(); b = new Point(); c = new Point(); d = new Point();
	}

	public boolean isIntersectingTriangle(Triangle T){
		//instantiating Point objects of the rectangle for compatibility with the
		//intersection method:
		a.x = x; a.y = y;//top left
		b.x = x + width; b.y = y;//top right
		c.x = x; c.y = y + height;//bottom left
		d.x = x + width; d.y = y + height;//bottom right
		//========================================================================

		//side a-b(T) & a-b(R)
		if(intersection(T.a, T.b, a, b)) return true;
		//side a-c(T) & a-b(R)
		if(intersection(T.a, T.c, a, b)) return true;
		//side b-c(T) & a-b(R)
		if(intersection(T.b, T.c, a, b)) return true;

		//side a-b(T) & a-c(R)
		if(intersection(T.a, T.b, a, c)) return true;
		//side a-c(T) & a-c(R)
		if(intersection(T.a, T.c, a, c)) return true;
		//side b-c(T) & a-c(R)
		if(intersection(T.b, T.c, a, c)) return true;

		//side a-b(T) & c-d(R)
		if(intersection(T.a, T.b, c, d)) return true;
		//side a-c(T) & c-d(R)
		if(intersection(T.a, T.c, c, d)) return true;
		//side b-c(T) & c-d(R)
		if(intersection(T.b, T.c, c, d)) return true;

		//side a-b(T) & b-d(R)
		if(intersection(T.a, T.b, b, d)) return true;
		//side a-c(T) & b-d(R)
		if(intersection(T.a, T.c, b, d)) return true;
		//side b-c(T) & b-d(R)
		if(intersection(T.b, T.c, b, d)) return true;

		//Tiangle within Rectangle
		if(triangleContained(T)) return true;
		//Rectangle within Triangle
		if(triangleContaining(T)) return true;

		//=========================================================================
		//no intersection
		return false;
	}

	private boolean triangleContained(Triangle T){
		if(T.a.x > x && T.a.x < x + width
				&& T.a.y > y && T.a.y < y + height) return true;//contained
		else return false;
	}

	private boolean triangleContaining(Triangle T){
		float alpha = ((T.b.y - T.c.y)*(x - T.c.x) + (T.c.x - T.b.x)*(y - T.c.y)) /
				((T.b.y - T.c.y)*(T.a.x - T.c.x) + (T.c.x - T.b.x)*(T.a.y - T.c.y));
		float beta = ((T.c.y - T.a.y)*(x - T.c.x) + (T.a.x - T.c.x)*(y - T.c.y)) /
				((T.b.y - T.c.y)*(T.a.x - T.c.x) + (T.c.x - T.b.x)*(T.a.y - T.c.y));
		float gamma = 1.0f - alpha - beta;

		if(alpha > 0 && beta > 0 && gamma > 0) return true;//contains
		else return false;
	}

	// a1 is line1 start, a2 is line1 end, b1 is line2 start, b2 is line2 end
	private static boolean intersection(Point a1, Point a2, Point b1, Point b2){

		Point b = subtract(a2,a1);
		Point d = subtract(b2,b1);
		float bDotDPerp = b.x * d.y - b.y * d.x;

		// if b dot d == 0, it means the lines are parallel so have infinite intersection points
		if (bDotDPerp == 0)
			return false;

		Point c = subtract(b1,a1);
		float t = (c.x * d.y - c.y * d.x) / bDotDPerp;
		if (t < 0 || t > 1)
			return false;

		float u = (c.x * b.y - c.y * b.x) / bDotDPerp;
		if (u < 0 || u > 1)
			return false;

		//Point intersection = Point.Sum(a1, Point.Multiply(b,t));

		return true;
	}

	private static Point subtract(Point a, Point b){
		return new Point(a.x - b.x, a.y - b.y);
	}
}

class Triangle{
	public Point a, b, c;

	public Triangle(){
		a = new Point();
		b = new Point();
		c = new Point();
	}

	public Triangle(Point a, Point b, Point c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
}

class Point{
	public int x, y;

	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}

	public Point(){
		x = 0;
		y = 0;
	}
}