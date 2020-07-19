BEGIN {
dropped=0;
udp_drop = 0
total = 0;
}
{
total++;
event =$1;
e= $5
if(event=="d")
{
if(e=="cbr" || e=="cbr1" || e=="cbr2")
udp_drop++;
dropped++;
}
}
END {
printf("Total dropped packets = %d",dropped);
printf("\nDropped UDP packets = %d",udp_drop);
printf("\nDropped TCP packets = %d",dropped - udp_drop);
printf("\nThroughput = %f",((total-dropped)/total)*100);
}
