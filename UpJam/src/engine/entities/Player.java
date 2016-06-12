package engine.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import map.Map;
import map.Tile;
import engine.Handler;

public class Player extends Entities {
	
	private boolean aPressed = false , wPressed = false, sPressed = false, dPressed = false;
	private int playerSpeed = 1;

	private Map map;
	


	private Handler handler;
	
	public Player(int xCoord, int yCoord, int size, Handler handler, Map map) {
		super(xCoord, yCoord, size);
		this.map = map;
		this.handler = handler;
		handler.addPlayer(this);

	}

	@Override
	public void tick() {
		setVelX(0);
		setVelY(0);
		int size = getSize();
		if(aPressed && !dPressed
				&& !map.getTile((int)((getXCoord()-playerSpeed+7)/size),(int)((getYCoord()+(size/2))/size)).isSolid()
				&& !map.getTile((int)((getXCoord()-playerSpeed+7)/size),(int)((getYCoord()+size)/size)).isSolid()){
			setVelX(-(int)(playerSpeed*getVelocityBuff()));
		}
		if(!aPressed && dPressed
				&& !map.getTile((int)((getXCoord()+playerSpeed+(size-7))/size),(int)((getYCoord()+(size/2))/size)).isSolid()
				&& !map.getTile((int)((getXCoord()+playerSpeed+(size-7))/size),(int)((getYCoord()+size)/size)).isSolid()){
			setVelX((int)(playerSpeed*getVelocityBuff()));
		}
		if(!wPressed && sPressed 
				&& !map.getTile((int)((getXCoord()+(size/2))/size),(int)((getYCoord()+playerSpeed+size)/size)).isSolid()){
			setVelY((int)(playerSpeed*getVelocityBuff()));
		}
		if(wPressed && !sPressed && !map.getTile((int)((getXCoord()+(size/2))/size), (int)((getYCoord()-playerSpeed+size/2)/size)).isSolid()){ 

					setVelY(-(int)(playerSpeed*getVelocityBuff()));
				
		}
		
		setXCoord(getXCoord() + getVelX());
		setYCoord(getYCoord() + getVelY());
	}
	

	@Override
	public void render(Graphics g) {
		
		if(wPressed){
			g.drawImage(new ImageIcon("resources/sprites/samBackSprite.png").getImage() , this.getXCoord(), this.getYCoord(), Tile.TILESIZE, Tile.TILESIZE,null);
		}else if(dPressed){
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(new ImageIcon("resources/sprites/sam.png").getImage() , this.getXCoord()+Tile.TILESIZE, this.getYCoord(), -Tile.TILESIZE, Tile.TILESIZE,null);
		}else{
			g.drawImage(new ImageIcon("resources/sprites/sam.png").getImage() , this.getXCoord(), this.getYCoord(), Tile.TILESIZE, Tile.TILESIZE,null);
			
		}
		
		//g.setColor(Color.DARK_GRAY);
		//g.fillRect(this.getXCoord(), this.getYCoord(), Tile.TILESIZE, Tile.TILESIZE);

	}
	
	public void setAPressed(boolean n){
		this.aPressed = n;
	}
	
	public void setWPressed(boolean n){
		this.wPressed = n;
	}
	
	public void setDPressed(boolean n){
		this.dPressed = n;
	}
	
	public void setSPressed(boolean n){
		this.sPressed = n;
	}


	public void dropTrap() {
		handler.addExtras(new Trap((((int)((getXCoord()+(Tile.TILESIZE/2))/Tile.TILESIZE))*Tile.TILESIZE)+(Tile.TILESIZE/4),(((int)((getYCoord()+(Tile.TILESIZE/2))/Tile.TILESIZE))*Tile.TILESIZE)+(Tile.TILESIZE/4),Tile.TILESIZE));	
	}
}
