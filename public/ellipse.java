class Point 
{
 private float x;
 private float y;
 
 public Point(float x, float y) 
 {
  this.x = x;
  this.y = y;
 }
 
 public float getX() 
 {
  return this.x;
 }
 
 public float getY() 
 {  
  return this.y;
 }  
}

void drawLine(Point p1, Point p2)
{
    line(p1.getX(),p1.getY(),p2.getX(),p2.getY());
}

class Ellipse
{
  private Point center; 
  private float a;
  private float b;
  
  public Ellipse(Point center, float a, float b) 
  {
    this.center = center;
    if (b > a)
    {
     throw new IllegalArgumentException("a must be not less than b"); 
    }
    this.a = a;
    this.b = b;
  }
 
 public Point getCenter()
 {
    return this.center; 
 }
 
 public void draw()
 {
   ellipse(this.center.getX(), this.center.getY(), a * 2, b * 2);
 }
 
 public Point tangencyp1(Point A)
 {
    float xA = A.getX() - this.center.getX();
    float yA = A.getY() - this.center.getY();
    float detDivBy4 = b*b - a*a*b*b/(xA*xA) + a*a*yA*yA/(xA*xA);    
    float y0 = (yA*a*a/(xA*xA) + sqrt(detDivBy4)) / (1 + a*a*yA*yA/(b*b*xA*xA));
    float x0 = (1 - yA*y0/(b*b)) * (a*a/xA);
    return new Point(x0 + this.center.getX(), y0 + this.center.getY());
 }
 
 public Point tangencyp2(Point A)
 {
    float xA = A.getX() - this.center.getX();
    float yA = A.getY() - this.center.getY();
    float detDivBy4 = b*b - a*a*b*b/(xA*xA) + a*a*yA*yA/(xA*xA);    
    float y0 = (yA*a*a/(xA*xA) - sqrt(detDivBy4)) / (1 + a*a*yA*yA/(b*b*xA*xA));
    float x0 = (1 - yA*y0/(b*b)) * (a*a/xA);
    return new Point(x0 + this.center.getX(), y0 + this.center.getY());
 }
}

Ellipse e;
//Ellipse's parameters
Point center;
float a;
float b;

//flag for user's curve input
boolean isCurveDrawn;
//points of curve
ArrayList points;


void setup() 
{
  size(700, 700);
  noFill();
  center = new Point(300, 300);
  a = 200;
  b = 100;
  e = new Ellipse(center, a, b);
  points = new ArrayList();
  isCurveDrawn = false;
  println("M1 = ");
  println("1 0 " + (-center.getX()));
  println("0 1 " + (-center.getY()));
  println("0 0 1");
  
  println();
  println("xA'   1 0 " + (-center.getX()) + "\t xA");
  println("yA' = 0 1 " + (-center.getY()) + "\t*yA");
  println("1     0 0 1\t 1");
  
  println();
  println("M2 = ");
  println("1 0 " + (center.getX()));
  println("0 1 " + (center.getY()));
  println("0 0 1");
  
  println();
  println("xA   1 0 " + center.getX() + "\t xA'");
  println("yA = 0 1 " + center.getY() + "\t*yA'");
  println("1     0 0 1\t 1");
}

void mouseDragged() 
{
  if(mouseButton == LEFT)
  {
    for(int i = 1; i < points.size(); i++)
    {
      Point p1 = (Point)points.get(i - 1);
      Point p2 = (Point)points.get(i);
      stroke(0, 0, 255);
      drawLine(p1, p2);
    }
    float x = mouseX;
    float y = mouseY;
    Point p = new Point(x,y);
    line(x, y, pmouseX, pmouseY);
    points.add(p);
  }
}

void mouseReleased()
{
  if (mouseButton == LEFT)
  { 
   isCurveDrawn = true; 
  }
}

int counter = 0;

void draw() 
{
  background(255);
  stroke(0);
  e.draw();  
  if (isCurveDrawn && counter < points.size())
  {
      stroke(0, 0, 255);
      for(int i = 1; i < points.size(); i++)
      {
        Point p1 = (Point)points.get(i - 1);
        Point p2 = (Point)points.get(i);
        drawLine(p1, p2);
      }   
      Point p = (Point)points.get(counter++);
      ellipse(p.getX(), p.getY(), 2, 2);
      Point t1 = e.tangencyp1(p);
      Point t2 = e.tangencyp2(p);
      stroke(255, 0, 0);
      drawLine(p, new Point(2*t1.getX() - p.getX(), 2*t1.getY() - p.getY()));
      drawLine(p, new Point(2*t2.getX() - p.getX(), 2*t2.getY() - p.getY()));
      ellipse(t1.getX(), t1.getY(), 2, 2);
      ellipse(t2.getX(), t2.getY(), 2, 2);
      if (counter == points.size())
      {
        isCurveDrawn = false;
        counter = 0;
        points.clear();
      }
  }
    
}