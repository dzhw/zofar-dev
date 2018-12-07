package eu.dzhw.zofar.management.dev;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Kickstart {

	public static void main(String[] args) {
		final Map<String,Class> toStart = new HashMap<String,Class>();
		toStart.put("create", eu.dzhw.zofar.management.dev.automation.createProject.main.CLIStarter.class);
		toStart.put("deploy", eu.dzhw.zofar.management.dev.automation.deploy.main.CLIStarter.class);
		toStart.put("reminder", eu.dzhw.zofar.management.dev.automation.reminder.main.CLIStarter.class);
		toStart.put("screenshot", eu.dzhw.zofar.management.dev.automation.screenshot.main.CLIStarter.class);
		
		if ((args != null) && (args.length > 0)) {
			final String target = args[0];
			if(toStart.containsKey(target)){
				final Class targetClass = toStart.get(target);
				
				Method mainMethod = null;
				try{
					final Class[] argTypes = { args.getClass(), };
					mainMethod = targetClass.getMethod("main", argTypes);
				}
				catch(NoSuchMethodException e){
					e.printStackTrace();
				}
				
				if(mainMethod != null){
					Object[] passedArgs = { Arrays.copyOfRange(args, 1, args.length) };
					try {
						mainMethod.invoke(null, passedArgs);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				else{
					System.err.println("There is no Main-Method in Class : "+targetClass.getName());
				}
			}
			else{
				System.err.println("Unkown Command : "+target+" Possibles : "+toStart.keySet());
			}
		}
		else{
			String path = Kickstart.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		
			System.err.println("Arguments missing: java -jar "+path+" "+toStart.keySet()+" [config=PATH_TO_CONFIG] [MORE PARAMETERS]");
		}

	}

}
