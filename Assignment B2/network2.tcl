set ns [new Simulator]
$ns color 1 Blue
$ns color 2 Red
set nf [open out.nam w]
$ns namtrace-all $nf


set nt [open test.tr w]
$ns trace-all $nt


proc finish {} { 
global ns nf nt
$ns flush-trace 
close $nf 
close $nt
exec nam out.nam & 
exit 0
} 
set n0 [$ns node]
set n1 [$ns node] 
set n2 [$ns node]
set n3 [$ns node]
set n4 [$ns node]
set n5 [$ns node]

$ns duplex-link $n2 $n0 2Mb 10ms DropTail
$ns duplex-link $n3 $n0 2Mb 10ms DropTail
$ns duplex-link $n1 $n0 2Mb 10ms DropTail
$ns duplex-link $n0 $n4 2Mb 10ms DropTail
$ns duplex-link $n0 $n5 2Mb 10ms DropTail

$ns queue-limit $n0 $n4 5
$ns queue-limit $n0 $n5 5

$ns duplex-link-op $n2 $n0 orient right-down
$ns duplex-link-op $n1 $n0 orient right
$ns duplex-link-op $n0 $n4 orient left-down
$ns duplex-link-op $n0 $n5 orient right-down
$ns duplex-link-op $n3 $n0 orient left-down

$ns duplex-link-op $n0 $n4 queuePos 0.8


set tcp [new Agent/TCP] 
$tcp set class_ 2
$ns attach-agent $n1 $tcp
set sink [new Agent/TCPSink] 
$ns attach-agent $n5 $sink
$ns connect $tcp $sink
$tcp set fid_ 1

set ftp [new Application/FTP] 
$ftp attach-agent $tcp 
$ftp set type_ FTP 



set udp [new Agent/UDP] 
$ns attach-agent $n2 $udp 
set null [new Agent/Null] 
$ns attach-agent $n4 $null 
$ns connect $udp $null 
$udp set fid_ 2

# Setup a CBR over UDP connection 
set cbr [new Application/Traffic/CBR] 
$cbr attach-agent $udp 
$cbr set type_ CBR 
$cbr set packet_size_ 1000
$cbr set rate_ 1mb
$cbr set random_ false 




$ns at 0.1 "$cbr start"

$ns at 0.6 "$ftp start"
#$ns at 1.0 "$ftpa start"

#$ns at 20.0 "$ftpa stop"
$ns at 20.0 "$ftp stop"

$ns at 20.5 "$cbr stop"


$ns at 4.5 "$ns detach-agent $n1 $tcp"
#$ns at 4.5 "$ns detach-agent $n7 $tcpa ; $ns detach-agent $n6 $sink"

# Call the finish procedure after 
# 5 seconds of simulation time 
$ns rtmodel-at 2.5 down $n0 $n5
$ns rtmodel-at 3.0 up $n0 $n5
$ns at 5.0 "finish"

# Print CBR packet size and interval 
puts "CBR packet size = [$cbr set packet_size_]"
puts "CBR interval = [$cbr set interval_]"

# Run the simulation 
$ns run 


