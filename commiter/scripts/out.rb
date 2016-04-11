#!/usr/bin/ruby

curfd = nil
oldln = nil
while ln = STDIN.gets do
        if oldln and (oldln.chomp == ln.chomp) then
            # avoid messy spec of php system() API call
            # http://php.net/manual/ja/function.system.php
        elsif ln =~ /^-+([\w]+)-+$/ then
                table = $1
                curfd.close if curfd
                curfd = File.open(ARGV[0] + "/" + table + ".csv", "w+")
        elsif curfd then
                curfd.puts ln.encode("UTF-8")
        end
        oldln = ln
end
