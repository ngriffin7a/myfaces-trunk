package net.sf.myfaces;

import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import net.sourceforge.myfaces.lifecycle.LifecycleImpl;
import java.util.Stack;


/*<privileged aspect*/abstract class/*>*/ PerformanceMeasuring
{
    private static final Stack _stack = new Stack();

    /*<pointcut*/abstract void/*>*/ lifecyclePhase(LifecycleImpl lc)
        /*: target(lc) &&
            (call(* reconstituteComponentTree(..)) ||
             call(* applyRequestValues(..)) ||
             call(* processValidations(..)) ||
             call(* updateModelValues(..)) ||
             call(* invokeApplication(..)) ||
             call(* renderResponse(..)))
        */;

    /*<before*/void before1/*>*/(LifecycleImpl lc)
        /*: lifecyclePhase(lc) */
    {
        entering();
    }

    /*<after*/void after1/*>*/(LifecycleImpl lc)
        /*: lifecyclePhase(lc) */
    {
        /*<*/Object thisJoinPoint = null;/*>*/
        leaving(thisJoinPoint.toString());
    }




    /*<before*/void before2/*>*/(LifecycleImpl lc)
        /*: target(lc) &&
            call(void execute(FacesContext)) */
    {
        entering();
        System.out.println();
        print("Lifecycle start");
    }

    /*<after*/void after2/*>*/(LifecycleImpl lc)
        /*: target(lc) &&
            call(void execute(FacesContext)) */
    {
        /*<*/Object thisJoinPoint = null;/*>*/
        leaving("Lifecycle end");
    }




    void entering()
    {
        _stack.push(new Long(System.currentTimeMillis()));
    }

    void leaving(String methodName)
    {
        long now = System.currentTimeMillis();
        Long enteringTime = (Long)_stack.peek();
        long diff = now - enteringTime.longValue();

        print(diff + " ms " + methodName);

        _stack.pop();
    }

    void print(String s)
    {
        System.out.println();
        for (int i = 0, len = _stack.size(); i < len; i++)
        {
            System.out.print(">");
        }
        System.out.print(" ");
        System.out.print(s);
    }


}



