import json

from enum import Enum
from NeatLogger import Log
from NeatLogger.Formatter.configs import DEFAULT_IGNORE_ATTRIBUTE_LIST

ignore_log_attribute_list = DEFAULT_IGNORE_ATTRIBUTE_LIST.copy()
ignore_log_attribute_list.remove("threadName")
ignore_log_attribute_list.append("name")
ignore_log_attribute_list.append("lineno")


sNL = Log(
    project_name="Distributed Systems",
    log_to_stdout=True,
    formatter="apache",
    log_level = 'info',
    ignore_log_attribute_list=ignore_log_attribute_list,
)


class Problem(Enum):
    q1 = 1
    q2 = 2
    q3 = 3
    q4 = 4
    q5 = 5
    q6 = 6
    q7 = 7
    q8 = 8
    q9 = 9
    nda = 10