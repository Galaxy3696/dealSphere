local voucherId = ARGV[1]
local userId = ARGV[2]

local stockKey = "seckill:stock" .. voucherId
local orderKey = "seckill:order" .. voucherId

-- 判断库存是否充足
if(tonumber(redis.call('get',stockKey))<0) then
     return 1
end
-- 判断用户是否下单
if(redis.call('sismember',orderKey,userId) == 1) then
     return 2  -- 重复下单
end

-- 扣库存
 redis.call('incrby',stockKey,-1)
 -- 3.5. 下单（保存用户）sadd orderKey userId
 redis.call('sadd', orderkey, userId)

 return 0