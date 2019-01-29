## JAVA_UDP


convert http connect to udp connect;


udpA-->send(right) -->get(right)

udpB-->send(right) -->get(right)

                      (wrap)                                                 (wrap)
UDPA   get(requestURI)------>UDP(send(requestURI))---->UDP(receive(response))------>send(response)
        
                                                (wrap)                           (wrap)
UDPB   UDP(send(ip))---->UDP(receive(requestURI))----->requestURI()------>response------>UDP(send(response))
            
            
server  receive(ip) as _ip
            
            get(..) new_ip----->send(..) _ip
            get(..) _ip----->send(..) new_ip

we can see that requestURI from A and B are the same,
define data x,wrap function f,transmit function t

          A                            B
so  x--->f(x)--->t[f(x)] --->t^t[f(x)]--->f^f(x)--->x

    A.x==B.x -=> A.f(x)==B.t^t[f(x)]
    
        x=f^f(x)   y=t^t[y]
        f^f=1  t^t=1
        f^f=t^t
        f^t^tf=1
        define t and f as tf
        (tf)^(tf)=1
        tf=tf
        (tf)^=f^t^
        predict (abcd..)^=...d^c^b^a^  //proved by dty717
        if m^m=1
        so  (abcd..m)^(abcd..m)=1
            (abcd..)^(abcd..)=1
            (abcd..)^m^m(abcd..)=1
    
        <=(abcd..m)^=m^...d^c^b^a^
        <=(abcd..m)^=m^(abcd..)^
        <=(abcd..m)^(abcd..)m=m^m
        <=(abcd..m)^(abcd..)m=(abcd..m)^(abcd..m)
        <=((abcd..)m=(abcd..m)
        
        so predict is true
    
    x  tf(x)  --->(tf)^tf(x)=f^t^tf(x)=x
        
        define f(x) y
        y=f(x)
        x=f^(y)
        (x,y) -->y=f(x)   (x--->y)
        (y,x) -->y=f^(x)  (y--->x)
        
    by the way,suppose many{x},many{y},
        find x1,x2 to y1,y2
        so x1-->y1 x2-->y2
        x1-->y2 =x1-->y1+y1-->y2
    
    and think a question
        from x -->y is there a "smallest" function to realize
        