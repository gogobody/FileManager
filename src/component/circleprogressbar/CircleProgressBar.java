package component.circleprogressbar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;


public class CircleProgressBar extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * ��С����ֵ��
	 */
	private int minimumProgress;

	/**
	 * ������ֵ��
	 */
	private int maximumProgress;

	/**
	 * ��ǰ����ֵ��
	 */
	private int progress;

	/**
	 * ������ɫ��
	 */
	private Color backgroundColor;

	/**
	 * ǰ����ɫ��
	 */
	private Color foregroundColor;

	/**
	 * ������ɫ��
	 */
	private Color digitalColor;

	/**
	 * ����һ��Բ�ν���������
	 */
	public CircleProgressBar() {
		setMinimumProgress(0);
		setMaximumProgress(100);
		setProgress(0);
		setBackgroundColor(new Color(209, 206, 200));
		setForegroundColor(new Color(172, 168, 163));
		setDigitalColor(Color.BLACK);
	}

	/**
	 * ����Բ�ν�������
	 * 
	 * @param g
	 *            ���ʡ�
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D graphics2d = (Graphics2D) g;
		// ���������
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		int fontSize = 0;
		if (getWidth() >= getHeight()) {
			x = (getWidth() - getHeight()) / 2 + 25;
			y = 25;
			width = getHeight() - 50;
			height = getHeight() - 50;
			fontSize = getWidth() / 8;
		} else {
			x = 25;
			y = (getHeight() - getWidth()) / 2 + 25;
			width = getWidth() - 50;
			height = getWidth() - 50;
			fontSize = getHeight() / 8;
		}
		graphics2d.setStroke(new BasicStroke(20.0f));
		graphics2d.setColor(backgroundColor);
		graphics2d.drawArc(x, y, width, height, 0, 360);
		graphics2d.setColor(foregroundColor);
		graphics2d.drawArc(x, y, width, height, 90, -(int) (360 * ((progress * 1.0) / (getMaximumProgress() - getMinimumProgress()))));
		graphics2d.setFont(new Font("����", Font.BOLD, fontSize));
		FontMetrics fontMetrics = graphics2d.getFontMetrics();
		int digitalWidth = fontMetrics.stringWidth(progress + "%");
		int digitalAscent = fontMetrics.getAscent();
		graphics2d.setColor(digitalColor);
		graphics2d.drawString(progress + "%", getWidth() / 2 - digitalWidth / 2, getHeight() / 2 + digitalAscent / 2);
	}

	/**
	 * ������С����ֵ��
	 * 
	 * @return ��С����ֵ��
	 */
	public int getMinimumProgress() {
		return minimumProgress;
	}

	/**
	 * ������С����ֵ��
	 * 
	 * @param minimumProgress
	 *            ��С����ֵ��
	 */
	public void setMinimumProgress(int minimumProgress) {
		if (minimumProgress <= getMaximumProgress()) {
			this.minimumProgress = minimumProgress;
		}
	}

	/**
	 * ����������ֵ��
	 * 
	 * @return ������ֵ��
	 */
	public int getMaximumProgress() {
		return maximumProgress;
	}

	/**
	 * ����������ֵ��
	 * 
	 * @param maximumProgress
	 *            ������ֵ��
	 */
	public void setMaximumProgress(int maximumProgress) {
		if (maximumProgress >= getMinimumProgress()) {
			this.maximumProgress = maximumProgress;
		}
	}

	/**
	 * ���ص�ǰ����ֵ��
	 * 
	 * @return ��ǰ����ֵ��
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * ���õ�ǰ����ֵ��
	 * 
	 * @param progress
	 *            ��ǰ����ֵ��
	 */
	public void setProgress(int progress) {
		if (progress >= getMinimumProgress() && progress <= getMaximumProgress()) {
			this.progress = progress;
			this.repaint();
		}
	}

	/**
	 * ���ر�����ɫ��
	 * 
	 * @return ������ɫ��
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * ���ñ�����ɫ��
	 * 
	 * @param backgroundColor
	 *            ������ɫ��
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.repaint();
	}

	/**
	 * ����ǰ����ɫ��
	 * 
	 * @return ǰ����ɫ��
	 */
	public Color getForegroundColor() {
		return foregroundColor;
	}

	/**
	 * ����ǰ����ɫ��
	 * 
	 * @param foregroundColor
	 *            ǰ����ɫ��
	 */
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
		this.repaint();
	}

	/**
	 * ����������ɫ��
	 * 
	 * @return ������ɫ��
	 */
	public Color getDigitalColor() {
		return digitalColor;
	}

	/**
	 * ����������ɫ��
	 * 
	 * @param digitalColor
	 *            ������ɫ��
	 */
	public void setDigitalColor(Color digitalColor) {
		this.digitalColor = digitalColor;
		this.repaint();
	}

}
