Example 3:

Conditional mapping using "set" with a "when" clause that is predicated on whether a xml element at the specified xml path is absent.

The attribute EngineType->capacityUnit is conditionally set from xml path usEngineType->usEngineSpecification->usEngineDetail->volumeCapacityUnit when there is no value at xml path usEngineType->usEngineSpecification->usEngineDetail->powerUnit.