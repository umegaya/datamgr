#!/usr/bin/ruby

curfd = nil
while ln = STDIN.gets do
        if ln =~ /^-+([\w]+)-+$/ then
                table = $1
                curfd.close if curfd
                curfd = File.open(ARGV[0] + "/" + table + ".csv", "w+")
        elsif curfd then
                curfd.puts ln.encode("UTF-8")
        end
end
