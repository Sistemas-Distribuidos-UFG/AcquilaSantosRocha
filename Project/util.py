def convert2serialize(obj):
    if isinstance(obj, dict):
        return { k: convert2serialize(v) for k, v in obj.items() }
    elif hasattr(obj, "_ast"):
        return convert2serialize(obj._ast())
    elif not isinstance(obj, str) and hasattr(obj, "__iter__"):
        return [ convert2serialize(v) for v in obj ]
    elif hasattr(obj, "__dict__"):
        return {
            k: convert2serialize(v)
            for k, v in obj.__dict__.items()
            if not callable(v) and not k.startswith('_') and k != 'data' and k != 'id' and k != 'rect'
        }
    else:
        return obj