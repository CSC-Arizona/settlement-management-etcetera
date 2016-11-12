/**
 * A very basic representation of a vector of two floats.
 * @author Artyom Perov.
 */

package entities;

public class Vec2f {
	public Vec2f(float x, float y){
		this.x = x;
		this.y = y;
	}

	/*
	 * Copy constructor.
	 */
	public Vec2f(Vec2f that){
		this.x = that.x;
		this.y = that.y;
	}

	/*
	 * Add two vectors.
	 */
	public Vec2f add(Vec2f that){
		return new Vec2f(this.x + that.x, this.y + that.y);
	}

	/*
	 * Subtract two vectors
	 */
	public Vec2f sub(Vec2f that){
		return new Vec2f(this.x - that.x, this.y - that.y);
	}

	/*
	 * Multiply by a scalar.
	 */
	public Vec2f mul(float scalar){
		return new Vec2f(this.x * scalar, this.y * scalar);
	}

	/*
	 * Calculate a dot product
	 */
	public float dot(Vec2f that){
		return this.x * that.x + this.y * that.y;
	}

	/*
	 * Returns square of the magnitude of a vector. Used for optimization, since
	 * sqrt is expensive.
	 */
	public float getMagSquared(){
		return (x * x) + (y * y);
	}
	
	/*
	 * Returns the magnitude of a vector.
	 */
	public float getMag(){
		return (float)Math.sqrt(getMagSquared());
	}

	// Default access modifier: seen within package
	float x;
	float y;
}

