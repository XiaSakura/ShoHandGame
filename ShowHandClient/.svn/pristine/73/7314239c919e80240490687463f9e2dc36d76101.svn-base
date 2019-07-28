package com.poker.service;

import java.awt.Point;

import com.poker.domain.Card;
/**
 * @author q9826
 *移动的静态函数：
 */
public class CMoving {
	public static void move(Card card, Point start, Point end)
	{
		if (start.x != end.x)
		{
			// 默认向左移动
			int speed = -10;
			if (start.x < end.x)
			{
				// 向右边移动
				speed = -speed;
			}
			
			double tangent = ((double) end.y - start.y) / Math.abs(end.x - start.x);
			while (Math.abs(end.x - start.x) > 20)
			{
				start.x += speed;
				start.y += Math.abs(speed) * tangent;
				card.setLocation(start.x, start.y);
				try
				{
					Thread.sleep(1);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			// 最后校准位置
			card.setLocation(end);
		}
	}

}
