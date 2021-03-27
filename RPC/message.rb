require "json"

module Message
	def dumps _hash, p
		unless _hash.nil? or _hash == {}

			return JSON.dump({problem:p, data: _hash})
		end
		return ''
	end

	def loads _string
		unless _string.nil?
			return JSON.load(_string)
		end
		return {}
	end

end
