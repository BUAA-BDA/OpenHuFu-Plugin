#include "aby4j/aby4j_party.h"

#include <memory>
#include <fstream>

namespace aby4j {
  Party::Party(uint32_t party_id, const std::string& server_addr, uint16_t server_port) {
    e_role r = party_id == 0? SERVER : CLIENT;
    party_ = new ABYParty(r, server_addr, server_port);
  }

  bool Party::GreaterI32(e_role role, int32_t val1, int32_t val2, bool shared) {
    party_->Reset();
    std::vector<Sharing*>& sharings = this->party_->GetSharings();
    Circuit* circ = sharings[S_BOOL]->GetCircuitBuildRoutine();
    share *s_alice, *s_bob, *s_out;
    uint32_t output;
    uint32_t alice = val1, bob = val2;
    s_alice = circ->PutSharedINGate(alice, 32);
    s_bob = circ->PutSharedINGate(bob, 32);
    s_out = circ->PutGTGate(s_alice, s_bob);
    if (shared) {
      s_out = circ->PutSharedOUTGate(s_out);
    }
    else {
      s_out = circ->PutOUTGate(s_out, ALL);
    }
    this->party_->ExecCircuit();
    output = s_out->get_clear_value<uint32_t>();
    return output;
  }

  int Party::MaxI32(e_role role, int32_t val1, int32_t val2, bool shared) {
    party_->Reset();
    std::vector<Sharing*>& sharings = this->party_->GetSharings();
    Circuit* circ = sharings[S_BOOL]->GetCircuitBuildRoutine();
    uint32_t output;
    uint32_t v1 = val1, v2 = val2;
    auto s_v1 = circ->PutSharedINGate(v1, 32);
    auto s_v2 = circ->PutSharedINGate(v2, 32);
    auto s_gt = circ->PutGTGate(s_v1, s_v2);
    auto s_out = circ->PutMUXGate(s_v1, s_v2, s_gt);
    
    if (shared) {
      s_out = circ->PutSharedOUTGate(s_out);
    }
    else {
      s_out = circ->PutOUTGate(s_out, ALL);
    }
    this->party_->ExecCircuit();
    output = s_out->get_clear_value<uint32_t>();
    std::ofstream fout("aby.log", std::ios::app);
    fout << std::hex << v1 << " " << v2 << " " << output << std::endl;
    delete party_;
    return output;
  }

  int Party::MinI32(e_role role, int32_t val1, int32_t val2, bool shared) {
    party_->Reset();
    std::vector<Sharing*>& sharings = this->party_->GetSharings();
    Circuit* circ = sharings[S_BOOL]->GetCircuitBuildRoutine();
    uint32_t output;
    uint32_t v1 = val1, v2 = val2;
    auto s_v1 = circ->PutSharedINGate(v1, 32);
    auto s_v2 = circ->PutSharedINGate(v2, 32);
    auto s_gt = circ->PutGTGate(s_v1, s_v2);
    auto s_out = circ->PutMUXGate(s_v2, s_v1, s_gt);
    
    if (shared) {
      s_out = circ->PutSharedOUTGate(s_out);
    }
    else {
      s_out = circ->PutOUTGate(s_out, ALL);
    }
    this->party_->ExecCircuit();
    output = s_out->get_clear_value<uint32_t>();
    return output;
  }
}