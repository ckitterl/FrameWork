package com.nd.utilities.logger;

public class Logger
{
	private static ILoggerStrategy m_logger = new LoggerStrategyDefault();
	private static String m_tag="";
	public static void setStrategy(ILoggerStrategy Strategy)
	{
		m_logger=Strategy;
	}
	public static void setTag(String tag)
	{
		m_tag=tag;
	}
	public static void d(Class<? extends Object> cls,String message)
	{
		
			m_logger.d(m_tag, formatMsg(cls,message));
	}
	public static void e(Class<? extends Object> cls,String message)
	{
		
			m_logger.e(m_tag, formatMsg(cls,message));
	}

	public static void i( Class<? extends Object> cls,String message)
	{
		
			m_logger.i(m_tag, formatMsg(cls,message));
	}

	public static void v( Class<? extends Object> cls,String message)
	{
		
			m_logger.v(m_tag, formatMsg(cls,message));
	}
	public static void w( Class<? extends Object> cls,String message)
	{
		
			m_logger.w(m_tag, formatMsg(cls,message));
	}
	public static void write(Class<? extends Object> cls,int level,  String message)
	{
		
			m_logger.write(level, m_tag, formatMsg(cls,message));
	}
	public static void debug(Class<? extends Object> cls,String message){
		
			m_logger.d(m_tag, formatMsg(cls,message));
	}
	private static String formatMsg(Class<? extends Object> cls,String message){
		return "["+cls.getName().substring(cls.getName().lastIndexOf('.')+1)+":"+new Exception().getStackTrace()[2].getMethodName()+"] : "+message;
	}
}
