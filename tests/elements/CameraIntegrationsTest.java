package elements;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests between the formation of beams from a camera
 * and the calculation of cuts of a beam with geometric bodies from the previous stage.
 */
public class CameraIntegrationsTest {
    /**
     * Private auxiliary function
     * @param cam camera
     * @param geo any shape
     * @param numOfIntersections The number of points of intersection with the shape
     */
    void assertTestCamera(Camera cam, Intersectable geo, int numOfIntersections) {
        cam.setViewPlaneSize(3, 3);
        cam.setDistance(1);

        int count = countIntersections(cam, geo);

        assertEquals(count, numOfIntersections, "the Actual Intersections is wrong");
    }

    /**
     *
     * Checks and returns how many points of intersection there are with the shape

     */
    private int countIntersections(Camera cam, Intersectable geo) {
        List<Intersectable.GeoPoint> all_intersections=null;
        int count=0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                var Actual = geo.findGeoIntersections(cam.constructRayThroughPixel(3, 3, j, i));
                if (Actual != null) {
                    if (all_intersections == null) {
                        all_intersections = new LinkedList<>();
                    }
                    all_intersections.addAll(Actual);
                }
                if (Actual== null)
                    count += 0;
                else
                    count += Actual.size();
            }
        }
        return count;
    }

    /**
     * Integration tests between the formation of
     * rays from a camera and the calculation of cuts of a  ray with Sphere
     */
    @Test
    void findIntersectionsWithSphere(){
        Camera camera1 = new Camera(Point3D.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0));
        Camera camera2 = new Camera(new Point3D(0, 0, 0.5), new Vector(0, 0, -1), new Vector(0, -1, 0));

        // 1: Small Sphere 2 points
        assertTestCamera(camera1, new Sphere(1, new Point3D(0, 0, -3)), 2);

        // 2: Big Sphere 18 points
        assertTestCamera(camera2, new Sphere(2.5, new Point3D(0, 0, -2.5)), 18);

        // 3: Medium Sphere 10 points
        assertTestCamera(camera2, new Sphere(2, new Point3D(0, 0, -2)), 10);

        // 4: Inside Sphere 9 points
        assertTestCamera(camera2, new Sphere(4, new Point3D(0, 0, -1)), 9);

        // 5: Beyond Sphere 0 points
        assertTestCamera(camera2, new Sphere(0.5, new Point3D(0, 0, 1)), 0);
    }
    /**
     * Integration tests between the formation of
     * rays from a camera and the calculation of cuts of a  ray with Plane
     */
    @Test
    void findIntersectionsWithPlane(){
        Camera camera1 = new Camera(Point3D.ZERO, new Vector(0, 0, -1), new Vector(0, -1, 0));

        // 1: Plane against camera 9 points
        assertTestCamera(camera1, new Plane(new Point3D(0, 0, -5), new Vector(0, 0, 1)), 9);

        // 2: Plane with small angle 9 points
        assertTestCamera(camera1, new Plane(new Point3D(0, 0, -5), new Vector(0, 1, 2)), 9);

        // 3: Plane parallel to lower rays 6 points
        assertTestCamera(camera1, new Plane(new Point3D(0, 0, -5), new Vector(0, 1, 1)), 6);

        // 4: Beyond Plane 0 points
        assertTestCamera(camera1, new Plane(new Point3D(0, 0, -5), new Vector(0, 1, 1)), 6);

    }
    /**
     * Integration tests between the formation of
     * rays from a camera and the calculation of cuts of a  ray with Triangle
     */
    @Test
    void findIntersectionsWithTriangle(){
        Camera camera1 = new Camera(Point3D.ZERO, new Vector(0, 0, -1), new Vector(0, -1, 0));

        // 1: Small triangle 1 point
        assertTestCamera(camera1, new Triangle(new Point3D(1, 1, -2), new Point3D(-1, 1, -2), new Point3D(0, -1, -2)), 1);

        // 2: Medium triangle 2 points
        assertTestCamera(camera1, new Triangle(new Point3D(1, 1, -2), new Point3D(-1, 1, -2), new Point3D(0, -20, -2)), 2);
    }

}

