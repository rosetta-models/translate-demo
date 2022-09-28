Example 2:

Conditional mapping using "set" with a "when" clause that is predicated on a xml element at the specified xml path exists.

The attribute EngineType->capacityUnit is conditionally set from xml path usEngineType->usEngineSpecification->usEngineDetail->volumeCapacityUnit when there is a value at xml path usEngineType->usEngineSpecification->usEngineDetail->powerUnit.